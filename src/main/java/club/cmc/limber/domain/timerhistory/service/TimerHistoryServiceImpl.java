package club.cmc.limber.domain.timerhistory.service;

import club.cmc.limber.domain.timerhistory.dto.history.*;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.repository.TimerHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
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
        // 기존 구현 유지 (참고: 전체에서 필터링 → 성능상 비권장이므로 점차 미사용 권장)
        return timerHistoryRepository.findAll()
                .stream()
                .filter(h -> h.getUserId().equals(userId) && "N".equals(h.getDelFlag()))
                .map(TimerHistoryResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Object searchWithRetrospect(TimerHistorySearchRequestDto req) {
        boolean onlyIncomplete = Boolean.TRUE.equals(req.onlyIncompleteRetrospect());

        // 전체 + 회고 유무 projection
        List<TimerHistoryWithRetrospectDto> rows = timerHistoryRepository.searchAllWithRetrospect(
                req.userId(), onlyIncomplete
        );

        //
        // Range 결정: ALL → 평평한 리스트, WEEKLY → 주별 그룹
        if (req.searchRange() == SearchRange.WEEKLY) {
            // 주 시작(월요일) 기준으로 묶기
            Map<LocalDate, List<TimerHistoryWithRetrospectDto>> grouped = rows.stream()
                    .collect(Collectors.groupingBy(r -> {
                        LocalDate d = r.historyDt().toLocalDate();
                        // ISO 기준: Monday가 1 → d에서 Monday로 이동
                        return d.with(DayOfWeek.MONDAY);
                    }, LinkedHashMap::new, Collectors.toList()));

            return grouped.entrySet().stream()
                    .map(e -> new TimerHistoryWeeklyGroupDto(
                            e.getKey(),
                            e.getKey().plusDays(6),
                            e.getValue()
                    ))
                    .toList();
        } else {
            return rows; // ALL
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> findLatestHistoryId(String userId, Long timerId) {
        return timerHistoryRepository
                .findTopByUserIdAndTimerIdAndDelFlagOrderByHistoryDtDescIdDesc(userId, timerId, "N")
                .map(TimerHistory::getId);
    }

}

