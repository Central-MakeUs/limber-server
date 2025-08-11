package club.cmc.limber.domain.timerhistory.dto;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record TimerHistoryResponseDto(
        Long id,
        Long timerId,
        String userId,
        String title,
        Long focusTypeId,
        RepeatCycleCode repeatCycleCode,
        String repeatDays,
        LocalDateTime historyDt,
        HistoryStatus historyStatus,
        String failReason,
        LocalTime startTime,
        LocalTime endTime
) {
    public static TimerHistoryResponseDto from(TimerHistory entity) {
        return new TimerHistoryResponseDto(
                entity.getId(),
                entity.getTimerId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getFocusTypeId(),
                entity.getRepeatCycleCode(),
                entity.getRepeatDays(),
                entity.getHistoryDt(),
                entity.getHistoryStatus(),
                entity.getFailReason(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }
}
