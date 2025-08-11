package club.cmc.limber.domain.timerhistory.repository;

import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface TimerHistoryRepository extends JpaRepository<TimerHistory, Long> {

    List<TimerHistory> findByUserIdAndStartTimeBetweenAndDelFlag(String userId, LocalTime startTime, LocalTime endTime, String delFlag);
    List<TimerHistory> findByUserIdAndDelFlag(String userId, String delFlag);

    List<TimerHistory> findByUserIdAndHistoryStatusAndDelFlag(String userId, HistoryStatus status, String delFlag);

    /**
     * SENT + delFlag='N' + actualStartTime이 [startDt, endDt] 구간에 '포함'되는 데이터
     * - 통계 (1)~(5) 공통 조회용
     */
    @Query("""
        SELECT h
          FROM TimerHistory h
         WHERE h.userId = :userId
           AND h.historyStatus = :status
           AND h.delFlag = 'N'
           AND h.actualStartTime BETWEEN :startDt AND :endDt
        """)
    List<TimerHistory> findByUserIdAndStatusAndActualStartBetween(
            @Param("userId") String userId,
            @Param("status") HistoryStatus status,   // 보통 HistoryStatus.SENT
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt
    );

    /**
     * FAILED + delFlag='N' + historyDt가 [startDt, endDt] 구간에 '포함'되는 데이터
     * - 실패 사유 집계 (6)용
     */
    @Query("""
        SELECT h
          FROM TimerHistory h
         WHERE h.userId = :userId
           AND h.historyStatus = :status
           AND h.delFlag = 'N'
           AND h.historyDt BETWEEN :startDt AND :endDt
        """)
    List<TimerHistory> findByUserIdAndStatusAndHistoryDtBetween(
            @Param("userId") String userId,
            @Param("status") HistoryStatus status,   // 보통 HistoryStatus.FAILED
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt
    );

}

