package club.cmc.limber.domain.timerhistory.service;


import club.cmc.limber.domain.timerhistory.dto.analytics.*;
import club.cmc.limber.domain.timerhistory.dto.history.FocusTimeSlice;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.FailReason;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import club.cmc.limber.domain.timerhistory.repository.TimerHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimerHistoryAnalyticsServiceImpl implements TimerHistoryAnalyticsService {

    private final TimerHistoryRepository repo;

    public TimerHistoryAnalyticsServiceImpl(TimerHistoryRepository repo) {
        this.repo = repo;
    }

    // ========== (1) 요일별 actual 합 ==========
    @Override
    @Transactional(readOnly = true)
    public List<WeekdayActualDto> getActualByWeekday(String userId, LocalDate startDate, LocalDate endDate) {
        var range = toRange(startDate, endDate);

        // SENT + actualStartTime이 기간에 "포함"되는 데이터만 DB에서 조회
        List<TimerHistory> rows = repo.findByUserIdAndStatusAndActualStartBetween(
                userId, HistoryStatus.SENT, range.start(), range.end());

        int[] actual = new int[7]; // 요일별 합(분)

        for (TimerHistory h : rows) {
            int mins = minutesBetween(h.getActualStartTime(), h.getActualEndTime());
            if (mins <= 0) continue;
            int idx = toWeekdayIndex(h.getActualStartTime().getDayOfWeek());
            actual[idx] += mins;
        }

        List<WeekdayActualDto> out = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            out.add(new WeekdayActualDto(i, fromWeekdayIndex(i), actual[i]));
        }
        return out;
    }

    // ========== (2) 요일별 몰입도 ==========
    @Override
    @Transactional(readOnly = true)
    public List<WeekdayImmersionDto> getImmersionByWeekday(String userId, LocalDate startDate, LocalDate endDate) {
        var range = toRange(startDate, endDate);
        List<TimerHistory> rows = repo.findByUserIdAndStatusAndActualStartBetween(
                userId, HistoryStatus.SENT, range.start(), range.end());

        int[] sched = new int[7];
        int[] act   = new int[7];

        for (TimerHistory h : rows) {
            int a = minutesBetween(h.getActualStartTime(), h.getActualEndTime());
            int s = minutesBetween(h.getStartTime(), h.getEndTime());
            if (a <= 0 && s <= 0) continue;
            int idx = toWeekdayIndex(h.getActualStartTime().getDayOfWeek());
            if (a > 0) act[idx] += a;
            if (s > 0) sched[idx] += s;
        }

        List<WeekdayImmersionDto> out = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            double ratio = sched[i] == 0 ? 0.0 : (double) act[i] / (double) sched[i];
            out.add(new WeekdayImmersionDto(i, fromWeekdayIndex(i), act[i], sched[i], round2(ratio)));
        }
        return out;
    }

    // ========== (3) 전체 actual 합 ==========
    @Override
    @Transactional(readOnly = true)
    public TotalActualDto getTotalActual(String userId, LocalDate startDate, LocalDate endDate) {
        var range = toRange(startDate, endDate);
        List<TimerHistory> rows = repo.findByUserIdAndStatusAndActualStartBetween(
                userId, HistoryStatus.SENT, range.start(), range.end());

        int total = rows.stream()
                .mapToInt(h -> minutesBetween(h.getActualStartTime(), h.getActualEndTime()))
                .filter(m -> m > 0)
                .sum();

        return new TotalActualDto(total, toHourMinLabel(total));
    }

    // ========== (4) 전체 몰입도 ==========
    @Override
    @Transactional(readOnly = true)
    public TotalImmersionDto getTotalImmersion(String userId, LocalDate startDate, LocalDate endDate) {
        var range = toRange(startDate, endDate);
        List<TimerHistory> rows = repo.findByUserIdAndStatusAndActualStartBetween(
                userId, HistoryStatus.SENT, range.start(), range.end());

        int totalAct = 0;
        int totalSched = 0;

        for (TimerHistory h : rows) {
            int a = minutesBetween(h.getActualStartTime(), h.getActualEndTime());
            int s = minutesBetween(h.getStartTime(), h.getEndTime());
            if (a > 0) totalAct += a;
            if (s > 0) totalSched += s;
        }
        double ratio = totalSched == 0 ? 0.0 : (double) totalAct / (double) totalSched;

        return new TotalImmersionDto(totalAct, totalSched, round2(ratio));
    }

    // ========== (5) FocusType별 actual 합 ==========
    @Override
    @Transactional(readOnly = true)
    public List<FocusDistributionDto> getFocusDistribution(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        var range = toRange(startDate, endDate);

        // 1) 쿼리에서 focusTypeName까지 받아오기
        List<FocusTimeSlice> rows = repo.findFocusSlices(userId, range.start(), range.end());

        // 2) focusTypeId별 분 합계 누적 (이름은 최초 값 유지)
        record Acc(String name, int total) {}
        Map<Long, Acc> acc = new HashMap<>();

        for (FocusTimeSlice r : rows) {
            int minutes = minutesBetween(r.getActualStartTime(), r.getActualEndTime());
            if (minutes <= 0 || r.getFocusTypeId() == null) continue;

            acc.compute(r.getFocusTypeId(), (id, prev) -> {
                if (prev == null) return new Acc(r.getFocusTypeTitle(), minutes);
                return new Acc(prev.name(), prev.total() + minutes);
            });
        }

        // 3) DTO 변환 + 정렬 (원하는 기준으로 변경 가능)
        return acc.entrySet().stream()
                .map(e -> new FocusDistributionDto(e.getKey(), e.getValue().name(), e.getValue().total()))
                .sorted(Comparator.comparingInt(FocusDistributionDto::totalActualMinutes).reversed()) // 총 소요분 내림차순
                .toList();
    }

    /** 자정 넘어가는 케이스까지 고려한 분 단위 차이 */
    private int minutesBetween(LocalTime start, LocalTime end) {
        if (start == null || end == null) return 0;
        if (end.isBefore(start)) { // 자정 경과
            return (int) Duration.between(start, end.plusHours(24)).toMinutes();
        }
        return (int) Duration.between(start, end).toMinutes();
    }

    // ========== (6) 실패 사유 (FAILED + historyDt 기간 포함) ==========
    @Override
    @Transactional(readOnly = true)
    public List<FailReasonCountDto> getFailReasonCounts(String userId, LocalDate startDate, LocalDate endDate) {
        var startDt = startDate.atStartOfDay();
        var endDt   = endDate.atTime(23, 59, 59);

        // 실패는 actual이 없을 수 있으므로 historyDt 기준으로 기간 필터링
        List<TimerHistory> failed = repo.findByUserIdAndStatusAndHistoryDtBetween(
                userId, HistoryStatus.FAILED, startDt, endDt);

        Map<FailReason, Long> counts = failed.stream()
                .collect(Collectors.groupingBy(
                        h -> Optional.ofNullable(h.getFailReason()).orElse(FailReason.NONE),
                        Collectors.counting()
                ));

        return counts.entrySet().stream()
                .map(e -> new FailReasonCountDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(FailReasonCountDto::failReason))
                .toList();
    }

    // ===== helpers =====

    private record Range(LocalDateTime start, LocalDateTime end) {}
    private static Range toRange(LocalDate start, LocalDate end) {
        return new Range(start.atStartOfDay(), end.atTime(23, 59, 59));
    }

    private static int minutesBetween(LocalDateTime s, LocalDateTime e) {
        if (s == null || e == null) return 0;
        return (int) Duration.between(s, e).toMinutes();
    }

    private static int toWeekdayIndex(DayOfWeek d) {
        return switch (d) {
            case SUNDAY -> 0; case MONDAY -> 1; case TUESDAY -> 2;
            case WEDNESDAY -> 3; case THURSDAY -> 4; case FRIDAY -> 5; case SATURDAY -> 6;
        };
    }
    private static DayOfWeek fromWeekdayIndex(int i) {
        return switch (i) {
            case 0 -> DayOfWeek.SUNDAY; case 1 -> DayOfWeek.MONDAY; case 2 -> DayOfWeek.TUESDAY;
            case 3 -> DayOfWeek.WEDNESDAY; case 4 -> DayOfWeek.THURSDAY; case 5 -> DayOfWeek.FRIDAY; case 6 -> DayOfWeek.SATURDAY;
            default -> throw new IllegalArgumentException("weekday index: " + i);
        };
    }

    private static String toHourMinLabel(int minutes) {
        int h = minutes / 60, m = minutes % 60;
        if (h == 0) return m + "분";
        if (m == 0) return h + "시간";
        return h + "시간 " + m + "분";
    }
    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
}