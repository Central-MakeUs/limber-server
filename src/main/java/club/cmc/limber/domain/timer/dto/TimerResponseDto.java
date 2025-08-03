package club.cmc.limber.domain.timer.dto;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;

import java.time.LocalTime;

public record TimerResponseDto(
        Long id,
        String title,
        Long focusTypeId,
        RepeatCycleCode repeatCycleCode,
        String repeatDays,
        LocalTime startTime,
        LocalTime endTime,
        TimerStatus status
) {}
