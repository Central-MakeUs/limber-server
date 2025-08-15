package club.cmc.limber.domain.timerhistory.repository;

import club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryWithRetrospectDto;
import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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
      h.id,                               -- Long id
      h.timerId,                          -- Long timerId
      h.userId,                           -- String userId
      h.title,                            -- String title
      h.focusTypeId,                      -- Long focusTypeId
      h.repeatCycleCode,                  -- RepeatCycleCode repeatCycleCode
      h.repeatDays,                       -- String repeatDays
      h.historyDt,                        -- LocalDateTime historyDt
      h.historyStatus,                    -- HistoryStatus historyStatus
      h.failReason,                       -- String failReason
      h.startTime,                        -- LocalTime startTime
      h.endTime,                          -- LocalTime endTime
      case when tr.id is not null then true else false end, -- boolean hasRetrospect
      tr.id,                              -- Long retrospectId
      tr.immersion,                       -- Integer retrospectImmersion
      tr.comment,                         -- String retrospectComment
      f.title,                            -- String focusTypeTitle
      null                                -- compact constructor에서 무시하고 직접 계산
    )
    from club.cmc.limber.domain.timerhistory.entity.TimerHistory h
      left join club.cmc.limber.domain.timerretrospect.entity.TimerRetrospect tr
        on tr.timerHistoryId = h.id and tr.delFlag = 'N'
      left join club.cmc.limber.domain.focus.entity.FocusType f
        on f.id = h.focusTypeId
    where h.userId = :userId
      and h.delFlag = 'N'
      and h.historyStatus = club.cmc.limber.domain.timerhistory.enums.HistoryStatus.SENT
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

}

