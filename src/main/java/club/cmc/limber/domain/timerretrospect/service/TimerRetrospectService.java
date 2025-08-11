package club.cmc.limber.domain.timerretrospect.service;

import club.cmc.limber.domain.timerretrospect.dto.TimerRetrospectRequestDto;
import club.cmc.limber.domain.timerretrospect.dto.TimerRetrospectResponseDto;


public interface TimerRetrospectService {
    TimerRetrospectResponseDto saveRetrospect(TimerRetrospectRequestDto dto);
    TimerRetrospectResponseDto deleteTimerRetrospect(Long timerRetrospectId);
}
