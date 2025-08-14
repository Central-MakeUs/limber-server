package club.cmc.limber.batch;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TimerScheduleEvaluator {
    private TimerScheduleEvaluator() {}

    /**
     * "0, 1, 3" → {0,1,3}
     * 공백/빈/null 모두 안전 처리.
     */
    public static Set<Integer> parseRepeatDays(String repeatDays) {
        if (repeatDays == null || repeatDays.isBlank()) return Set.of();
        return Stream.of(repeatDays.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Java DayOfWeek(1=월..7=일) → 요구 포맷(0=일..6=토)로 매핑
     * 일요일(7) → 0
     * 월(1)→1, ..., 토(6)→6
     */
    public static int toZeroBasedDow(DayOfWeek dow) {
        return (dow == DayOfWeek.SUNDAY) ? 0 : dow.getValue();
    }

    /**
     * 오늘 실행 대상인지 판단
     */
    public static boolean isEligibleToday(RepeatCycleCode cycle, String repeatDays, LocalDate today) {
        int dow0 = toZeroBasedDow(today.getDayOfWeek()); // 0=일..6=토
        Set<Integer> days = parseRepeatDays(repeatDays);

        switch (cycle) {
            case EVERY:
                return true;

            case WEEKDAY:
                // 월(1)~금(5)
                return dow0 >= 1 && dow0 <= 5;

            case WEEKEND:
                // 토(6), 일(0)
                return dow0 == 0 || dow0 == 6;

            case NONE:
                // repeatDays 비었으면: 일회용 → 오늘 무조건 허용(시간이 맞는 순간 1회 실행 후 OFF)
                // repeatDays 있으면: 지정 요일에만 허용
                return days.isEmpty() || days.contains(dow0);

            default:
                return false;
        }
    }

    /** 일회용 판단: NONE 이고 repeatDays 비었을 때 */
    public static boolean isOneOff(RepeatCycleCode cycle, String repeatDays) {
        return cycle == RepeatCycleCode.NONE && (repeatDays == null || repeatDays.isBlank());
    }
}
