package club.cmc.limber.domain.timerhistory.repository;

import club.cmc.limber.domain.timerhistory.entity.TimerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerHistoryRepository extends JpaRepository<TimerHistory, Long> {
}

