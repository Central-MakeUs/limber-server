package club.cmc.limber.domain.timer.dto;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record TimerRequestDto(
        Long userId,
        String title,
        Long focusTypeId,
        RepeatCycleCode repeatCycleCode,
        String repeatDays,
        LocalTime startTime,
        LocalTime endTime
) {}

