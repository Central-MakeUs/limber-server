package club.cmc.limber.domain.timerretrospect.service;

import club.cmc.limber.domain.timerhistory.dto.TimerHistoryRequestDto;
import club.cmc.limber.domain.timerhistory.dto.TimerHistoryResponseDto;
import club.cmc.limber.domain.timerretrospect.dto.TimerRetrospectRequestDto;
import club.cmc.limber.domain.timerretrospect.dto.TimerRetrospectResponseDto;

import java.util.List;


public interface TimerRetrospectService {
    TimerRetrospectResponseDto saveRetrospect(Long userId, TimerRetrospectRequestDto dto);
}
