package club.cmc.limber.domain.timerhistory.dto.history;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.FailReason;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

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
        FailReason failReason,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(type = "string", example = "2025-08-16 19:00:00")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(type = "string", example = "2025-08-16 21:00:00")
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
