package club.cmc.limber.domain.timerhistory.repository;

import club.cmc.limber.domain.timerhistory.dto.history.FocusTimeSlice;
import club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryWithRetrospectDto;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimerHistoryRepository extends JpaRepository<TimerHistory, Long> {

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

    @Query("""
        SELECT h
          FROM TimerHistory h
         WHERE h.userId = :userId
           AND h.delFlag = 'N'
           AND h.actualStartTime BETWEEN :startDt AND :endDt
        """)
    List<TimerHistory> findByUserIdAndActualStartBetween(
            @Param("userId") String userId,
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

    @Query("""
    select new club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryWithRetrospectDto(
      h.id,
      h.timerId,
      h.userId,
      h.title,
      h.focusTypeId,
      h.repeatCycleCode,
      h.repeatDays,
      h.historyDt,
      h.historyStatus,
      h.failReason,
      h.startTime,
      h.endTime,
      case when tr.id is not null then true else false end,
      tr.id,
      tr.immersion,
      tr.comment,
      f.title,
      ''
    )
    from club.cmc.limber.domain.timerhistory.entity.TimerHistory h
      left join club.cmc.limber.domain.timerretrospect.entity.TimerRetrospect tr
        on tr.timerHistoryId = h.id and tr.delFlag = 'N'
      left join club.cmc.limber.domain.focus.entity.FocusType f
        on f.id = h.focusTypeId
    where h.userId = :userId
      and h.delFlag = 'N'
      and (
            (:onlyIncomplete = false)
         or (:onlyIncomplete = true and tr.id is null)
      )
    order by h.historyDt desc, h.id desc
    """)
    List<TimerHistoryWithRetrospectDto> searchAllWithRetrospect(
            @Param("userId") String userId,
            @Param("onlyIncomplete") boolean onlyIncomplete
    );
//    and h.historyStatus = club.cmc.limber.domain.timerhistory.enums.HistoryStatus.SENT


    // 동일 타이머/동일 분(±59초) 기록 중복 방지
    @Query("""
        select case when count(h) > 0 then true else false end
        from TimerHistory h
        where h.timerId = :timerId
          and h.historyDt >= :slotStart
          and h.historyDt < :slotEnd
          and h.delFlag = 'N'
    """)
    boolean existsInMinuteSlot(Long timerId, LocalDateTime slotStart, LocalDateTime slotEnd);

    Optional<TimerHistory> findTopByUserIdAndTimerIdAndDelFlagOrderByHistoryDtDescIdDesc(
            String userId, Long timerId, String delFlag
    );

    @Query("""
    select new club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryWithRetrospectDto(
      h.id,
      h.timerId,
      h.userId,
      h.title,
      h.focusTypeId,
      h.repeatCycleCode,
      h.repeatDays,
      h.historyDt,
      h.historyStatus,
      h.failReason,
      h.startTime,
      h.endTime,
      case when tr.id is not null then true else false end,
      tr.id,
      tr.immersion,
      tr.comment,
      f.title,
      ''
    )
    from club.cmc.limber.domain.timerhistory.entity.TimerHistory h
      left join club.cmc.limber.domain.timerretrospect.entity.TimerRetrospect tr
        on tr.timerHistoryId = h.id and tr.delFlag = 'N'
      left join club.cmc.limber.domain.focus.entity.FocusType f
        on f.id = h.focusTypeId
    where h.id = :historyId
    """)
    TimerHistoryWithRetrospectDto searchWithRetrospectByHistoryId(Long historyId);

    @Query("""
    select 
      h.focusTypeId                                as focusTypeId,
      f.title                                      as focusTypeTitle,
      cast(h.actualStartTime as time)              as actualStartTime,
      cast(h.actualEndTime   as time)              as actualEndTime   
    from club.cmc.limber.domain.timerhistory.entity.TimerHistory h
      left join club.cmc.limber.domain.focus.entity.FocusType f
        on f.id = h.focusTypeId
    where h.userId = :userId
      and h.delFlag = 'N'
      and h.actualStartTime between :start and :end
    """)
    List<FocusTimeSlice> findFocusSlices(
            @Param("userId") String userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    //      and h.historyStatus = club.cmc.limber.domain.timerhistory.enums.HistoryStatus.SENT

    void deleteAllByUserId(String userId);

    boolean existsByUserId(String userId);

}

