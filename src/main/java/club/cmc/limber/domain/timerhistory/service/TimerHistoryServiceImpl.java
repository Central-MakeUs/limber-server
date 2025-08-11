package club.cmc.limber.domain.timerhistory.service;

import club.cmc.limber.domain.timerhistory.dto.TimerHistoryRequestDto;
import club.cmc.limber.domain.timerhistory.dto.TimerHistoryResponseDto;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.repository.TimerHistoryRepository;
import club.cmc.limber.domain.timerretrospect.repository.TimerRetrospectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimerHistoryServiceImpl implements TimerHistoryService {

    private final TimerHistoryRepository timerHistoryRepository;

    public TimerHistoryServiceImpl(
            TimerHistoryRepository timerHistoryRepository
    ) {
        this.timerHistoryRepository = timerHistoryRepository;
    }

    @Override
    @Transactional
    public TimerHistoryResponseDto saveHistory(TimerHistoryRequestDto dto) {
        TimerHistory history = new TimerHistory();
        history.setTimerId(dto.timerId());
        history.setUserId(dto.userId());
        history.setTitle(dto.title());
        history.setFocusTypeId(dto.focusTypeId());
        history.setRepeatCycleCode(dto.repeatCycleCode());
        history.setRepeatDays(dto.repeatDays());
        history.setHistoryDt(dto.historyDt());
        history.setHistoryStatus(dto.historyStatus());
        history.setFailReason(dto.failReason());
        history.setStartTime(dto.startTime());
        history.setEndTime(dto.endTime());
        history.setDelFlag("N");
        history.setRegDt(LocalDateTime.now());
        history.setRegId(dto.regId());

        TimerHistory saved = timerHistoryRepository.save(history);

        return new TimerHistoryResponseDto(
                saved.getId(),
                saved.getTimerId(),
                saved.getUserId(),
                saved.getTitle(),
                saved.getFocusTypeId(),
                saved.getRepeatCycleCode(),
                saved.getRepeatDays(),
                saved.getHistoryDt(),
                saved.getHistoryStatus(),
                saved.getFailReason(),
                saved.getStartTime(),
                saved.getEndTime()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimerHistoryResponseDto> getHistoriesByUserId(String userId) {
        return timerHistoryRepository.findAll()
                .stream()
                .filter(h -> h.getUserId().equals(userId) && "N".equals(h.getDelFlag()))
                .map(TimerHistoryResponseDto::from)
                .collect(Collectors.toList());
    }

}

