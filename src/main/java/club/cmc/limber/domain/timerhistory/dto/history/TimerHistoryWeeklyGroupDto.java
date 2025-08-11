package club.cmc.limber.domain.timerhistory.dto.history;


import java.time.LocalDate;
import java.util.List;

public record TimerHistoryWeeklyGroupDto(
        LocalDate weekStart,   // (월요일 기준) 주 시작
        LocalDate weekEnd,     // 주 종료 (일요일)
        List<TimerHistoryWithRetrospectDto> items
) {}
