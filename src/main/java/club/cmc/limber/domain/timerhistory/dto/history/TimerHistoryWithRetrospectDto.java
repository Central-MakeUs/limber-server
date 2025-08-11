package club.cmc.limber.domain.timerhistory.dto.history;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record TimerHistoryWithRetrospectDto(
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
        LocalTime endTime,
        boolean hasRetrospect,
        Long retrospectId
) {}
