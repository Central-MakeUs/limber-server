package club.cmc.limber.domain.timerhistory.service;

import club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryRequestDto;
import club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryResponseDto;
import club.cmc.limber.domain.timerhistory.dto.history.TimerHistorySearchRequestDto;
import club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryWithRetrospectDto;

import java.util.List;


public interface TimerHistoryService {

    TimerHistoryResponseDto saveHistory(TimerHistoryRequestDto dto);

    // 기존 메서드(필요 시 유지)
    List<TimerHistoryResponseDto> getHistoriesByUserId(String userId);

    // 새 검색 메서드 (ALL: List<TimerHistoryWithRetrospectDto>, WEEKLY: List<TimerHistoryWeeklyGroupDto>)
    Object searchWithRetrospect(TimerHistorySearchRequestDto req);

    TimerHistoryWithRetrospectDto getLatestHistoryWithRetrospect(String userId, Long timerId);

}
