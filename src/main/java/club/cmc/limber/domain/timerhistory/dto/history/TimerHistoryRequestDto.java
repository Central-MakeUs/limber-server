package club.cmc.limber.domain.timerhistory.dto.history;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timerhistory.enums.FailReason;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

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
        FailReason failReason,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @DateTimeFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "19:00")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @DateTimeFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "19:00")
        LocalTime endTime,
        String regId
) {}
