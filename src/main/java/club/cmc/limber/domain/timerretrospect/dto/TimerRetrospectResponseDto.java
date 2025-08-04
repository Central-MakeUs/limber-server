package club.cmc.limber.domain.timerretrospect.dto;

import club.cmc.limber.domain.timerretrospect.entity.TimerRetrospect;

public record TimerRetrospectResponseDto(
        Long id,
        Long timerHistoryId,
        Long timerId,
        Long userId,
        Integer immersion,
        String comment,
        String delFlag
) {
    public static TimerRetrospectResponseDto from(TimerRetrospect entity) {
        return new TimerRetrospectResponseDto(
                entity.getId(),
                entity.getTimerHistoryId(),
                entity.getTimerId(),
                entity.getUserId(),
                entity.getImmersion(),
                entity.getComment(),
                entity.getDelFlag()
        );
    }
}
