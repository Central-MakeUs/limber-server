package club.cmc.limber.domain.timer.service;

import club.cmc.limber.domain.timer.dto.*;
import club.cmc.limber.domain.timer.enums.TimerStatus;

import java.time.LocalTime;
import java.util.List;

public interface TimerService {
    TimerResponseDto createTimer(TimerRequestDto dto);
    List<TimerResponseDto> getTimersByUserId(String userId);
    TimerResponseDto updateTimerStatus(Long timerId, TimerStatusUpdateDto dto);
    void updateTimersStatusByUserAndCode(SpecificTimerStatusUpdateDto dto);
    TimerStatus getTimerStatus(Long timerId);
    TimerResponseDto getTimerById(Long timerId);
    void deleteTimer(Long timerId);
    void deleteTimer(TimerDeleteDto timerDeleteDto);
    boolean hasOverlappingRunningTimer(String userId, LocalTime startTime, LocalTime endTime, Long originalTimerId);
}
