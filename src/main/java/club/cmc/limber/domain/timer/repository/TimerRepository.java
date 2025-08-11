package club.cmc.limber.domain.timer.repository;


import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimerRepository extends JpaRepository<Timer, Long> {

    List<Timer> findByUserIdAndDelFlag(Long userId, String delFlag);

    List<Timer> findByUserIdAndStatusAndDelFlag(Long userId, String status, String delFlag);

    long countByUserIdAndDelFlagAndTimerCode(Long userId, String delFlag, TimerCode timerCode);
}