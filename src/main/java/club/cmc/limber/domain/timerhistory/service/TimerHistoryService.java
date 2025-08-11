package club.cmc.limber.domain.timerhistory.service;

import club.cmc.limber.domain.timerhistory.dto.TimerHistoryRequestDto;
import club.cmc.limber.domain.timerhistory.dto.TimerHistoryResponseDto;

import java.util.List;


public interface TimerHistoryService {

    TimerHistoryResponseDto saveHistory(TimerHistoryRequestDto dto);

    List<TimerHistoryResponseDto> getHistoriesByUserId(String userId);
}
