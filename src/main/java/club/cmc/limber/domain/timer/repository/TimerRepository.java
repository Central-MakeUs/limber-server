package club.cmc.limber.domain.timer.repository;


import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimerRepository extends JpaRepository<Timer, Long> {

    List<Timer> findByUserIdAndDelFlag(String userId, String delFlag);

    List<Timer> findByUserIdAndStatusAndDelFlag(String userId, TimerStatus status, String delFlag);

    long countByUserIdAndDelFlagAndTimerCode(String userId, String delFlag, TimerCode timerCode);
}