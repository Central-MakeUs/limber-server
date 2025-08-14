package club.cmc.limber.domain.timer.service;


import club.cmc.limber.domain.focus.entity.FocusType;
import club.cmc.limber.domain.focus.exception.FocusTypeNotFoundException;
import club.cmc.limber.domain.focus.repository.FocusTypeRepository;
import club.cmc.limber.domain.timer.dto.TimerRequestDto;
import club.cmc.limber.domain.timer.dto.TimerResponseDto;
import club.cmc.limber.domain.timer.dto.TimerStatusUpdateDto;
import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import club.cmc.limber.domain.timer.exception.TimerConflictException;
import club.cmc.limber.domain.timer.exception.TimerLimitExceededException;
import club.cmc.limber.domain.timer.exception.TimerNotFoundException;
import club.cmc.limber.domain.timer.repository.TimerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TimerServiceImpl implements TimerService {

    private final TimerRepository timerRepository;
    private final FocusTypeRepository focusTypeRepository;

    public TimerServiceImpl(TimerRepository timerRepository, FocusTypeRepository focusTypeRepository) {
        this.timerRepository = timerRepository;
        this.focusTypeRepository = focusTypeRepository;
    }

    @Override
    @Transactional
    public TimerResponseDto createTimer(TimerRequestDto dto) {
        FocusType focusType = focusTypeRepository.findById(dto.focusTypeId())
                .orElseThrow(FocusTypeNotFoundException::new);

        // IMMEDIATE 타입만 조회하여 개수 제한
        if (TimerCode.IMMEDIATE.equals(dto.timerCode())) {
            long immediateCount = timerRepository.countByUserIdAndDelFlagAndTimerCode(
                    dto.userId(),
                    "N",
                    TimerCode.IMMEDIATE
            );

            if (immediateCount >= 10) {
                throw new TimerLimitExceededException();
            }
        }

        boolean overlap = hasOverlappingRunningTimer(dto.userId(), dto.startTime(), dto.endTime(), 0L);
        if (overlap) {
            throw new TimerConflictException();
        }

        Timer timer = new Timer();
        timer.setUserId(dto.userId());
        timer.setTitle(dto.title());
        timer.setTimerCode(dto.timerCode());
        timer.setFocusType(focusType);
        timer.setRepeatCycleCode(dto.repeatCycleCode());
        timer.setRepeatDays(dto.repeatDays());
        timer.setStartTime(dto.startTime());
        timer.setEndTime(dto.endTime());
        timer.setStatus(TimerStatus.ON);
        timer.setDelFlag("N");
        timer.setRegId(dto.userId());

        Timer saved = timerRepository.save(timer);
        return toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimerResponseDto> getTimersByUserId(String userId) {
        return timerRepository.findByUserIdAndDelFlag(userId, "N")
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TimerResponseDto updateTimerStatus(Long timerId, TimerStatusUpdateDto dto) {
        Timer timer = getTimerOrThrow(timerId);

        if (dto.status() == TimerStatus.ON) {
            boolean overlap = hasOverlappingRunningTimer(
                    timer.getUserId(),
                    timer.getStartTime(),
                    timer.getEndTime(),
                    timerId
            );
            if (overlap) {
                throw new TimerConflictException();
            }
        }

        timer.setStatus(dto.status());
        return toResponseDto(timer);
    }

    private TimerResponseDto toResponseDto(Timer timer) {
        return new TimerResponseDto(
                timer.getId(),
                timer.getTitle(),
                timer.getFocusType().getId(),
                timer.getRepeatCycleCode(),
                timer.getRepeatDays(),
                timer.getStartTime(),
                timer.getEndTime(),
                timer.getStatus()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TimerStatus getTimerStatus(Long timerId) {
        return getTimerOrThrow(timerId).getStatus();
    }

    @Override
    @Transactional(readOnly = true)
    public TimerResponseDto getTimerById(Long timerId) {
        return toResponseDto(getTimerOrThrow(timerId));
    }

    @Override
    @Transactional
    public void deleteTimer(Long timerId) {
        Timer timer = getTimerOrThrow(timerId);
        timer.setDelFlag("Y");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasOverlappingRunningTimer(
            String userId,
            LocalTime start,
            LocalTime end,
            Long originalTimerId
    ) {
        List<Timer> runningTimers =
                timerRepository.findByUserIdAndStatusAndDelFlag(
                        userId,
                        TimerStatus.ON,
                        "N"
                );

        return runningTimers.stream()
                .filter(timer -> !Objects.equals(timer.getId(), originalTimerId))
                .anyMatch(t ->
                        (start.isBefore(t.getEndTime()) && end.isAfter(t.getStartTime()))
        );
    }


    // 공통: ID로 조회, 없으면 TimerNotFoundException
    @Transactional(readOnly = true)
    protected Timer getTimerOrThrow(Long timerId) {
        return timerRepository.findById(timerId)
                .orElseThrow(TimerNotFoundException::new);
    }
}
