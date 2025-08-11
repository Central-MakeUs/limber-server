package club.cmc.limber.domain.timerhistory.service;

import club.cmc.limber.domain.timerhistory.dto.*;
import club.cmc.limber.domain.timerhistory.dto.analytics.FailReasonCountDto;
import club.cmc.limber.domain.timerhistory.dto.analytics.FocusDistributionDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface TimerHistoryService {

    TimerHistoryResponseDto saveHistory(TimerHistoryRequestDto dto);

    List<TimerHistoryResponseDto> getHistoriesByUserId(String userId);

    // SENT: actual 구간이 기간과 겹치는 이력 목록
}
