package club.cmc.limber.domain.timer.dto;

import club.cmc.limber.domain.timer.enums.TimerCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;

public record SpecificTimerStatusUpdateDto(
        String userId,
        TimerCode timerCode,
        TimerStatus status
) {}

