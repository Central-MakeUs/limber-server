package club.cmc.limber.batch;

import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import club.cmc.limber.domain.timer.repository.TimerRepository;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import club.cmc.limber.domain.timerhistory.repository.TimerHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimerMinuteBatch {

    private static final ZoneId ZONE_SEOUL = ZoneId.of("Asia/Seoul");

    private final TimerRepository timerRepository;
    private final TimerHistoryRepository timerHistoryRepository;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void run() {
        LocalDateTime now = LocalDateTime.now(ZONE_SEOUL).withSecond(0).withNano(0);
        LocalTime nowHHmm = now.toLocalTime();
        LocalDate today = now.toLocalDate();
        log.info("[TIMER] tick now={} HH:mm:ss={}", now, nowHHmm);

        // endTime == 현재, status=ON
        List<Timer> candidates = timerRepository.findByStatusAndEndTime(TimerStatus.ON, nowHHmm);
        if (candidates.isEmpty()) return;

        for (Timer t : candidates) {
            // 오늘 대상 아니면 skip
            if (!TimerScheduleEvaluator.isEligibleToday(t.getRepeatCycleCode(), t.getRepeatDays(), today)) {
                continue;
            }

            // 동일 분 중복 방지
            LocalDateTime slotStart = now;
            LocalDateTime slotEnd = now.plusMinutes(1);
            if (timerHistoryRepository.existsInMinuteSlot(t.getId(), slotStart, slotEnd)) {
                log.debug("duplicate history skipped: timerId={}, minSlot={}", t.getId(), now);
                continue;
            }

            // 실제 시작/종료
            LocalDateTime actualStart = LocalDateTime.of(today, t.getStartTime());
            LocalDateTime actualEnd = now;

            String repeatDaysSafe = (t.getRepeatDays() == null) ? "" : t.getRepeatDays();

            // 정상 완료 → SENT
            HistoryStatus historyStatus = HistoryStatus.SENT;

            TimerHistory history = TimerHistory.builder()
                    .timerId(t.getId())
                    .userId(t.getUserId())
                    .title(t.getTitle())
                    .focusTypeId(t.getFocusType().getId())
                    .repeatCycleCode(t.getRepeatCycleCode())
                    .repeatDays(repeatDaysSafe)
                    .historyDt(now)
                    .historyStatus(historyStatus)
                    .failReason(null)
                    .startTime(t.getStartTime())
                    .endTime(t.getEndTime())
                    .actualStartTime(actualStart) // @Column(name="ACUTAL_START_TIME")
                    .actualEndTime(actualEnd)     // @Column(name="ACUTAL_END_TIME")
                    .delFlag("N")
                    .regId("SYSTEM_MINUTE_BATCH")
                    .build();

            timerHistoryRepository.save(history);

            // 일회용이면 OFF 전환
            if (TimerScheduleEvaluator.isOneOff(t.getRepeatCycleCode(), t.getRepeatDays())) {
                t.setStatus(TimerStatus.OFF);
                t.setUpdId("SYSTEM_MINUTE_BATCH");
            }

            log.info("history saved: timerId={}, userId={}, status={}, oneOff={}",
                    t.getId(), t.getUserId(), historyStatus,
                    TimerScheduleEvaluator.isOneOff(t.getRepeatCycleCode(), t.getRepeatDays()));
        }
    }
}
