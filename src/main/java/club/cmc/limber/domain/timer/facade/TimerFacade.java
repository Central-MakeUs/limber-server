package club.cmc.limber.domain.timer.facade;

import club.cmc.limber.domain.timer.dto.TimerUnlockRequestDto;
import club.cmc.limber.domain.timer.dto.TimerUnlockResponseDto;
import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import club.cmc.limber.domain.timer.exception.TimerNotFoundException;
import club.cmc.limber.domain.timer.repository.TimerRepository;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import club.cmc.limber.domain.timerhistory.repository.TimerHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TimerFacade {

    private final TimerRepository timerRepository;
    private final TimerHistoryRepository timerHistoryRepository;

    public TimerFacade(TimerRepository timerRepository, TimerHistoryRepository timerHistoryRepository) {
        this.timerRepository = timerRepository;
        this.timerHistoryRepository = timerHistoryRepository;
    }

    @Transactional
    public TimerUnlockResponseDto unlockTimer(TimerUnlockRequestDto dto) {
        // 1) 타이머 조회
        Timer timer = timerRepository.findByIdAndDelFlag(dto.timerId(), "N")
                .orElseThrow(TimerNotFoundException::new);

        // 2) 상태 전환 (잠금 해제 → OFF)
        timer.setStatus(TimerStatus.OFF);

        // 3) 이력 적재 (NOT NULL 필드 채우기)
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        // TimerHistory의 START/END는 LocalDateTime, Timer는 LocalTime이므로 오늘 날짜와 합성
        LocalDateTime plannedStart = LocalDateTime.of(today, timer.getStartTime());
        LocalDateTime plannedEnd   = LocalDateTime.of(today, timer.getEndTime());

        TimerHistory history = TimerHistory.builder()
                .timerId(timer.getId())
                .userId(timer.getUserId())
                .title(timer.getTitle())
                .focusTypeId(timer.getFocusType().getId())
                .repeatCycleCode(timer.getRepeatCycleCode())
                .repeatDays(timer.getRepeatDays())

                .historyDt(now)
                // 프로젝트 enum에 맞춰 주세요: 실패/중단 성격이면 FAILED/STOPPED 등
                .historyStatus(HistoryStatus.FAILED)

                .failReason(dto.failReason())

                .startTime(timer.getStartTime())
                .endTime(timer.getEndTime())
                // ACUTAL_* 컬럼은 NOT NULL 제약이 있으므로 기본값 보정
                // 실제 시작 시각을 모를 경우 계획 시작 시각으로 기록
                .actualStartTime(plannedStart)
                // 잠금 해제 시각을 실제 종료로
                .actualEndTime(now)

                .delFlag("N")
                .regId(timer.getUserId()) // 등록자 정책에 맞게 조정 가능
                .build();

        timerHistoryRepository.save(history);

        // 4) 응답 변환
        return TimerUnlockResponseDto.from(timer);
    }

}
