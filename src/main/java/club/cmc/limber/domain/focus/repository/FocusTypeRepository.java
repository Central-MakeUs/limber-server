package club.cmc.limber.domain.focus.repository;


import club.cmc.limber.domain.focus.entity.FocusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FocusTypeRepository extends JpaRepository<FocusType, Long> {

    List<FocusType> findByUserIdAndDelFlag(Long userId, String delFlag);

    List<FocusType> findByUserIdOrderBySequenceAsc(Long userId);

    boolean existsByUserIdAndDefaultFlag(Long userId, String defaultFlag);

    @Query("""
        SELECT f FROM FocusType f
        WHERE f.delFlag = 'N' AND f.userId = :userId
           OR f.defaultFlag = 'Y'
        ORDER BY 
            CASE WHEN f.defaultFlag = 'Y' THEN 0 ELSE 1 END,
            f.sequence ASC
        """)
    List<FocusType> findCustomFocusTypes(@Param("userId") Long userId);

}