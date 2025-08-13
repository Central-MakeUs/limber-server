package club.cmc.limber.domain.timer.service;


import club.cmc.limber.domain.focus.entity.FocusType;
import club.cmc.limber.domain.focus.repository.FocusTypeRepository;
import club.cmc.limber.domain.timer.dto.TimerRequestDto;
import club.cmc.limber.domain.timer.dto.TimerResponseDto;
import club.cmc.limber.domain.timer.dto.TimerStatusUpdateDto;
import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 집중 유형 타입입니다."));

        // IMMEDIATE 타입만 조회하여 개수 제한
        if (TimerCode.IMMEDIATE.equals(dto.timerCode())) {
            long immediateCount = timerRepository.countByUserIdAndDelFlagAndTimerCode(
                    dto.userId(),
                    "N",
                    TimerCode.IMMEDIATE
            );

            if (immediateCount >= 10) {
                throw new IllegalStateException("지금 시작은 최대 10개까지만 등록할 수 있습니다.");
            }
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
        Timer timer = timerRepository.findById(timerId)
                .orElseThrow(() -> new IllegalArgumentException("타이머가 존재하지 않습니다."));

        if (dto.status() == TimerStatus.ON) {
            boolean overlap = hasOverlappingRunningTimer(timer.getUserId(), timer.getStartTime(), timer.getEndTime());
            if (overlap) {
                throw new IllegalStateException("해당 시간에 이미 진행 중인 타이머가 존재합니다.");
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
        Timer timer = timerRepository.findById(timerId)
                .orElseThrow(() -> new IllegalArgumentException("타이머가 존재하지 않습니다."));
        return timer.getStatus();
    }

    @Override
    @Transactional(readOnly = true)
    public TimerResponseDto getTimerById(Long timerId) {
        Timer timer = timerRepository.findById(timerId)
                .orElseThrow(() -> new IllegalArgumentException("타이머가 존재하지 않습니다."));
        return toResponseDto(timer);
    }

    @Override
    @Transactional
    public void deleteTimer(Long timerId) {
        Timer timer = timerRepository.findById(timerId)
                .orElseThrow(() -> new IllegalArgumentException("타이머가 존재하지 않습니다."));
        timer.setDelFlag("Y");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasOverlappingRunningTimer(
            String userId,
            LocalTime start,
            LocalTime end
    ) {
        List<Timer> runningTimers =
                timerRepository.findByUserIdAndStatusAndDelFlag(
                        userId,
                        TimerStatus.ON,
                        "N"
                );
        return runningTimers.stream().anyMatch(t ->
                (start.isBefore(t.getEndTime()) && end.isAfter(t.getStartTime()))
        );
    }
}
