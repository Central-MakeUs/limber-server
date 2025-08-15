package club.cmc.limber.domain.timer.dto;

import club.cmc.limber.domain.timerhistory.enums.FailReason;

public record TimerUnlockRequestDto(
        Long timerId,
        FailReason failReason
) {}
