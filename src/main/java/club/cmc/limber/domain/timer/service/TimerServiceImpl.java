package club.cmc.limber.domain.timer.service;


import club.cmc.limber.domain.focus.entity.FocusType;
import club.cmc.limber.domain.focus.exception.FocusTypeNotFoundException;
import club.cmc.limber.domain.focus.repository.FocusTypeRepository;
import club.cmc.limber.domain.timer.dto.SpecificTimerStatusUpdateDto;
import club.cmc.limber.domain.timer.dto.TimerRequestDto;
import club.cmc.limber.domain.timer.dto.TimerResponseDto;
import club.cmc.limber.domain.timer.dto.TimerStatusUpdateDto;
import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import club.cmc.limber.domain.timer.exception.TimerConflictException;
import club.cmc.limber.domain.timer.exception.TimerNotFoundException;
import club.cmc.limber.domain.timer.exception.WrongTimerDeleteRequestParameterException;
import club.cmc.limber.domain.timer.repository.TimerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
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

        boolean overlap = hasOverlappingRunningTimer(
                dto.userId(),
                dto.startTime(),
                dto.endTime(),
                0L
        );
        if (overlap) {
            if (dto.timerCode().equals(TimerCode.SCHEDULED))
                timer.setStatus(TimerStatus.OFF);
            else
                throw new TimerConflictException();
        }

        Timer saved = timerRepository.save(timer);
        return toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimerResponseDto> getTimersByUserId(String userId) {
        return timerRepository.findByUserIdAndDelFlagAndTimerCode(userId, "N", TimerCode.SCHEDULED)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TimerResponseDto updateTimerStatus(Long timerId, TimerStatusUpdateDto dto) {
        Timer timer = getTimerOrThrow(timerId);

        if (dto.status() == TimerStatus.ON) {
            if (hasOverlappingRunningTimer(
                    timer.getUserId(),
                    timer.getStartTime(),
                    timer.getEndTime(),
                    timerId
            ))
                throw new TimerConflictException();
        }

        timer.setStatus(dto.status());
        return toResponseDto(timer);
    }

    @Override
    @Transactional
    public void updateTimersStatusByUserAndCode(
            SpecificTimerStatusUpdateDto dto
    ) {
        timerRepository.findByUserIdAndDelFlagAndTimerCode(dto.userId(), "N", dto.timerCode())
                .forEach(timer -> timer.setStatus(dto.status()));
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
    @Transactional // readOnly=false
    public void deleteTimer(List<Long> timerIds) {
        if (timerIds == null || timerIds.isEmpty())
            throw new WrongTimerDeleteRequestParameterException();

        timerIds.forEach(timerId -> {
            Timer timer = getTimerOrThrow(timerId);
            timer.setDelFlag("Y");
            timer.setStatus(TimerStatus.OFF);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasOverlappingRunningTimer(
            String userId,
            LocalTime startTime,
            LocalTime endTime,
            Long originalTimerId
    ) {
        Long excludeId = (originalTimerId == null) ? -1L : originalTimerId; // IdNot 사용 대비
        return timerRepository
                .existsByUserIdAndStatusAndDelFlagAndIdNotAndStartTimeBeforeAndEndTimeAfter(
                        userId, TimerStatus.ON, "N", excludeId, endTime, startTime
                );
    }

    // 공통: ID로 조회, 없으면 TimerNotFoundException
    @Transactional(readOnly = true)
    protected Timer getTimerOrThrow(Long timerId) {
        return timerRepository.findById(timerId)
                .orElseThrow(TimerNotFoundException::new);
    }
}
