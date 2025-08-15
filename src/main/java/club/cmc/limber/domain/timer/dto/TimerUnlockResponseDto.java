package club.cmc.limber.domain.timer.dto;


import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerStatus;

public record TimerUnlockResponseDto(
        Long id,
        String userId,
        String title,
        TimerStatus status
) {
    public static TimerUnlockResponseDto from(Timer t) {
        return new TimerUnlockResponseDto(t.getId(), t.getUserId(), t.getTitle(), t.getStatus());
    }
}
