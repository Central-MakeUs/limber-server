package club.cmc.limber.domain.focus.repository;


import club.cmc.limber.domain.focus.entity.FocusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FocusTypeRepository extends JpaRepository<FocusType, Long> {

    List<FocusType> findByUserIdAndDelFlag(Long userId, String delFlag);

    List<FocusType> findByUserIdOrderBySequenceAsc(Long userId);

    boolean existsByUserIdAndDefaultFlag(Long userId, String defaultFlag);

}