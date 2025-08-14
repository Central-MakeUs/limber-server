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

@Slf4j
@Component
@RequiredArgsConstructor
public class TimerMinuteBatch {

    private static final ZoneId ZONE_SEOUL = ZoneId.of("Asia/Seoul");

    private final TimerRepository timerRepository;
    private final TimerHistoryRepository timerHistoryRepository;

    /**
     * 매 분 0초에 실행 (초 단위까지 맞추어 분 정각에 수행)
     * 형식: 초 분 시 * * *  → "0 * * * * *"
     */
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void run() {
        // 분 정각(초/나노 0) 기준 시간 계산
        LocalDateTime now = LocalDateTime.now(ZONE_SEOUL)
                .withSecond(0).withNano(0);
        LocalTime nowHHmm = now.toLocalTime();             // HH:mm:00
        LocalDate today = now.toLocalDate();

        // status=ON && endTime == 현재 분(HH:mm)
        var targets = timerRepository.findByStatusAndEndTime(TimerStatus.ON, nowHHmm);

        if (targets.isEmpty()) {
            return;
        }

        for (Timer t : targets) {
            // 동일 분 중복 방지 ( [now, now+59초] 구간 )
            LocalDateTime slotStart = now;
            LocalDateTime slotEnd = now.plusMinutes(1); // [start, end) 반구간

            boolean exists = timerHistoryRepository.existsInMinuteSlot(t.getId(), slotStart, slotEnd);
            if (exists) {
                log.debug("skip duplicate history: timerId={}, slot={}", t.getId(), now);
                continue;
            }

            // 실제 시작/종료 시간 계산:
            // - 실제 시작: 오늘 날짜 + timer.startTime
            // - 실제 종료: 지금(now) 혹은 오늘 날짜 + timer.endTime (둘 다 동일 분이므로 now 사용)
            LocalDateTime actualStart = LocalDateTime.of(today, t.getStartTime());
            LocalDateTime actualEnd = now; // 분 배치 시점

            // repeatDays가 null일 수 있으므로 안전 처리 (History 컬럼은 not null)
            String repeatDays = (t.getRepeatDays() == null) ? "" : t.getRepeatDays();

            // HistoryStatus는 정책에 맞게 선택
            // - 정상 종료로 간주 → SUCCESS (혹은 COMPLETED 등 프로젝트 enum에 맞게 교체)
            HistoryStatus status = HistoryStatus.SENT;

            TimerHistory history = TimerHistory.builder()
                    .timerId(t.getId())
                    .userId(t.getUserId())
                    .title(t.getTitle())
                    .focusTypeId(t.getFocusType().getId())
                    .repeatCycleCode(t.getRepeatCycleCode())
                    .repeatDays(repeatDays)
                    .historyDt(now)               // 기록 생성 시각(분 정각)
                    .historyStatus(status)
                    .failReason(null)
                    .startTime(t.getStartTime())
                    .endTime(t.getEndTime())
                    .actualStartTime(actualStart)
                    .actualEndTime(actualEnd)
                    .delFlag("N")
                    .regId("SYSTEM_MINUTE_BATCH")
                    .build();

            timerHistoryRepository.save(history);
            log.info("saved history: timerId={}, userId={}, at={}", t.getId(), t.getUserId(), now);
        }
    }
}
