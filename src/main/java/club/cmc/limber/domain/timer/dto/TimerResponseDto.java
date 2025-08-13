package club.cmc.limber.domain.timer.dto;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

public record TimerResponseDto(
        Long id,
        String title,
        Long focusTypeId,
        RepeatCycleCode repeatCycleCode,
        String repeatDays,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @DateTimeFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "19:00")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @DateTimeFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "21:00")
        LocalTime endTime,
        TimerStatus status
) {}
