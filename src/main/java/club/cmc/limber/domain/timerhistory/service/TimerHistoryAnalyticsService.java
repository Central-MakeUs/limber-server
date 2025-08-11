package club.cmc.limber.domain.timerhistory.service;

import club.cmc.limber.domain.timerhistory.dto.analytics.*;

import java.time.LocalDate;
import java.util.List;

public interface TimerHistoryAnalyticsService {

    /** (1) 요일별 총 실험 시간: actualStart~actualEnd 합 (SENT + actualStartTime 기간 포함) */
    List<WeekdayActualDto> getActualByWeekday(String userId, LocalDate startDate, LocalDate endDate);

    /** (2) 요일별 총 몰입도: (actual 합 / scheduled 합) (SENT + actualStartTime 기간 포함) */
    List<WeekdayImmersionDto> getImmersionByWeekday(String userId, LocalDate startDate, LocalDate endDate);

    /** (3) 전체 총 실험 시간: actual 합 (SENT + actualStartTime 기간 포함) */
    TotalActualDto getTotalActual(String userId, LocalDate startDate, LocalDate endDate);

    /** (4) 전체 몰입도: (actual 합 / scheduled 합) (SENT + actualStartTime 기간 포함) */
    TotalImmersionDto getTotalImmersion(String userId, LocalDate startDate, LocalDate endDate);

    /** (5) 몰입 유형(RepeatCycleCode)별 실제 시간 합: actual 합 (SENT + actualStartTime 기간 포함) */
    List<FocusDistributionDto> getFocusDistribution(String userId, LocalDate startDate, LocalDate endDate);

    /** (6) 실패 사유 카운트: FAILED + historyDt 기간 포함 */
    List<FailReasonCountDto> getFailReasonCounts(String userId, LocalDate startDate, LocalDate endDate);
}
