package club.cmc.limber.domain.timerhistory.dto.history;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record TimerHistoryRequestDto(
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
        String regId
) {
    public TimerHistory toEntity() {
        TimerHistory entity = new TimerHistory();
        entity.setTimerId(timerId);
        entity.setUserId(userId);
        entity.setTitle(title);
        entity.setFocusTypeId(focusTypeId);
        entity.setRepeatCycleCode(repeatCycleCode);
        entity.setRepeatDays(repeatDays);
        entity.setHistoryDt(historyDt);
        entity.setHistoryStatus(historyStatus);
        entity.setFailReason(failReason);
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);
        entity.setDelFlag("N");
        entity.setRegDt(LocalDateTime.now());
        entity.setRegId(regId);
        return entity;
    }
}
