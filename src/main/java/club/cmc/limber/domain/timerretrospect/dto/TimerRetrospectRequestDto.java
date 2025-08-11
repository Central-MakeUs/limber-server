package club.cmc.limber.domain.timerretrospect.dto;


import club.cmc.limber.domain.timerretrospect.entity.TimerRetrospect;

import java.time.LocalDateTime;

public record TimerRetrospectRequestDto(
        String userId,
        Long timerHistoryId,
        Long timerId,
        Integer immersion,
        String comment
) {
    public TimerRetrospect toEntity() {
        TimerRetrospect entity = new TimerRetrospect();
        entity.setTimerHistoryId(timerHistoryId);
        entity.setTimerId(timerId);
        entity.setUserId(userId);
        entity.setImmersion(immersion);
        entity.setComment(comment);
        entity.setHistoryDt(LocalDateTime.now());
        entity.setRegDt(LocalDateTime.now());
        entity.setRegId(userId.toString());
        entity.setDelFlag("N");
        return entity;
    }
}
