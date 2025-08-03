package club.cmc.limber.domain.timer.dto;

import club.cmc.limber.domain.timer.enums.TimerStatus;

public record TimerStatusUpdateDto(
        TimerStatus status
) {}

