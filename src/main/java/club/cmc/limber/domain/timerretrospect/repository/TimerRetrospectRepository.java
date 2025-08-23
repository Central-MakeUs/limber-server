package club.cmc.limber.domain.timerretrospect.repository;

import club.cmc.limber.domain.timerretrospect.entity.TimerRetrospect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerRetrospectRepository extends JpaRepository<TimerRetrospect, Long> {

    void deleteAllByUserId(String userId);

    boolean existsByUserId(String userId);

}

