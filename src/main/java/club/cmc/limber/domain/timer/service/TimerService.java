package club.cmc.limber.domain.timer.service;

import club.cmc.limber.domain.timer.dto.TimerRequestDto;
import club.cmc.limber.domain.timer.dto.TimerResponseDto;
import club.cmc.limber.domain.timer.dto.TimerStatusUpdateDto;
import club.cmc.limber.domain.timer.enums.TimerStatus;

import java.time.LocalTime;
import java.util.List;

public interface TimerService {
    TimerResponseDto createTimer(TimerRequestDto dto);
    List<TimerResponseDto> getTimersByUserId(String userId);
    TimerResponseDto updateTimerStatus(Long timerId, TimerStatusUpdateDto dto);
    TimerStatus getTimerStatus(Long timerId);
    TimerResponseDto getTimerById(Long timerId);
    void deleteTimer(Long timerId);
    boolean hasOverlappingRunningTimer(String userId, LocalTime start, LocalTime end);
}
