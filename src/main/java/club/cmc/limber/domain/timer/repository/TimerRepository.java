package club.cmc.limber.domain.timer.repository;


import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimerRepository extends JpaRepository<Timer, Long> {

    Optional<Timer> findByIdAndDelFlag(Long id, String delFlag);

    List<Timer> findByUserIdAndDelFlagAndTimerCode(String userId, String delFlag, TimerCode timerCode);

    @Query("""
    select case when count(t) > 0 then true else false end
    from Timer t
    where t.userId = :userId
      and t.status = :status
      and t.delFlag = 'N'
      and t.id <> :excludeId
      and (
            -- 새 구간이 자정 안 넘는 경우
            (:newStart < :newEnd and
                (
                    (t.startTime < t.endTime and :newStart < t.endTime and :newEnd > t.startTime)
                 or (t.startTime >= t.endTime and (:newStart < t.endTime or :newEnd > t.startTime))
                )
            )
         or
            -- 새 구간이 자정 넘는 경우
            (:newStart >= :newEnd and
                (
                    (t.startTime < t.endTime and (:newStart < t.endTime or :newEnd > t.startTime))
                 or (t.startTime >= t.endTime)
                )
            )
      )
    """)
    boolean existsOverlappingTimer(
            @Param("userId") String userId,
            @Param("status") TimerStatus status,
            @Param("excludeId") Long excludeId,
            @Param("newStart") LocalTime newStart,
            @Param("newEnd") LocalTime newEnd
    );

    List<Timer> findByUserIdAndStatusAndDelFlag(String userId, TimerStatus status, String delFlag);

    long countByUserIdAndDelFlagAndTimerCode(String userId, String delFlag, TimerCode timerCode);

    List<Timer> findByStatusAndEndTime(TimerStatus status, LocalTime endTime);
}