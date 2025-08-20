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

        // 현재 시간과 예약 시작 ~ 예약 종료 겹치는지 판별
        if (dto.timerCode().equals(TimerCode.SCHEDULED) && isNowWithinRange(dto.startTime(), dto.endTime())) {
            timer.setStatus(TimerStatus.OFF);
        }

        Timer saved = timerRepository.save(timer);
        return toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TimerResponseDto> getTimersByUserId(String userId) {
        return timerRepository.findByUserIdAndDelFlagAndTimerCode(userId, "N", TimerCode.SCHEDULED)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TimerResponseDto updateTimerStatus(Long timerId, TimerStatusUpdateDto dto) {
        // 1) 대상 조회
        final Timer timer = getTimerOrThrow(timerId);
        final TimerStatus newStatus = dto.status();

        // 2) 변화 없음 → 빠른 반환
        if (timer.getStatus() == newStatus) {
            return toResponseDto(timer);
        }

        // 3) ON으로 전환 시에만 충돌 검증
        if (newStatus == TimerStatus.ON) {
            assertNoConflictsWhenTurningOn(timer, timerId);
        }

        // 4) 상태 업데이트 후 응답
        timer.setStatus(newStatus);
        return toResponseDto(timer);
    }

    /**
     * 타이머를 ON으로 전환할 때의 충돌 조건을 검사한다.
     * - 현재 시간과 예약 구간이 겹치면 안 됨
     * - 동일 사용자 기준 다른 실행 중 타이머와 예약 구간이 겹치면 안 됨
     * 충돌 시 TimerConflictException 발생.
     */
    private void assertNoConflictsWhenTurningOn(Timer timer, Long selfTimerId) {
        boolean overlapsNow = isNowWithinRange(timer.getStartTime(), timer.getEndTime());
        boolean overlapsOthers = hasOverlappingRunningTimer(
                timer.getUserId(),
                timer.getStartTime(),
                timer.getEndTime(),
                selfTimerId
        );

        if (overlapsNow || overlapsOthers) {
            throw new TimerConflictException();
        }
    }

    @Transactional
    @Override
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

    @Transactional(readOnly = true)
    @Override
    public TimerStatus getTimerStatus(Long timerId) {
        return getTimerOrThrow(timerId).getStatus();
    }

    @Transactional(readOnly = true)
    @Override
    public TimerResponseDto getTimerById(Long timerId) {
        return toResponseDto(getTimerOrThrow(timerId));
    }

    @Transactional // readOnly=false
    @Override
    public void deleteTimer(List<Long> timerIds) {
        if (timerIds == null || timerIds.isEmpty())
            throw new WrongTimerDeleteRequestParameterException();

        timerIds.forEach(timerId -> {
            Timer timer = getTimerOrThrow(timerId);
            timer.setDelFlag("Y");
            timer.setStatus(TimerStatus.OFF);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasOverlappingRunningTimer(
            String userId,
            LocalTime startTime,
            LocalTime endTime,
            Long originalTimerId
    ) {
        Long excludeId = (originalTimerId == null) ? -1L : originalTimerId; // IdNot 사용 대비
        return timerRepository.existsOverlappingTimer(userId, TimerStatus.ON, excludeId, startTime, endTime);
    }

    /**
     * 현재 시간이 [start, end] 범위에 포함되는지 여부를 분 단위로 판별한다.
     * - start == end 인 경우: 24시간 전체 포함(true)로 간주
     * - 끝점 포함 규칙: [start, end]
     * - 자정 넘김(start > end)도 지원: 예) 23:00~01:00 → 23:00~24:00, 00:00~01:00
     */
    public static boolean isNowWithinRange(LocalTime start, LocalTime end) {
        return isWithinRange(start, end, LocalTime.now());
    }

    public static boolean isWithinRange(LocalTime start, LocalTime end, LocalTime now) {
        // 같은 시각 → 24시간으로 간주
        if (start.equals(end)) {
            return true;
        }

        if (start.isBefore(end)) {
            // 같은 날 안에서 끝남: [start, end]
            return !now.isBefore(start) && !now.isAfter(end);
        } else {
            // 자정을 넘어감: [start, 24:00] ∪ [00:00, end]
            return !now.isBefore(start) || !now.isAfter(end);
        }
    }


    // 공통: ID로 조회, 없으면 TimerNotFoundException
    @Transactional(readOnly = true)
    protected Timer getTimerOrThrow(Long timerId) {
        return timerRepository.findById(timerId)
                .orElseThrow(TimerNotFoundException::new);
    }
}
