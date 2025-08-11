package club.cmc.limber.domain.timerretrospect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TIMER_RETROSPECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimerRetrospect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIMER_HISTORY_ID", nullable = false)
    private Long timerHistoryId;

    @Column(name = "TIMER_ID", nullable = false)
    private Long timerId;

    @Column(name = "USER_ID", nullable = false, length = 255)
    private String userId;

    @Column(name = "IMMERSION", nullable = false)
    private Integer immersion;

    @Column(name = "COMMENT", nullable = false, length = 255)
    private String comment;

    @Column(name = "HISTORY_DT", nullable = false)
    private LocalDateTime historyDt;

    @Column(name = "DEL_FLAG", nullable = false)
    private String delFlag = "N";

    @Column(name = "REG_DT", nullable = false)
    private LocalDateTime regDt;

    @Column(name = "REG_ID", nullable = false)
    private String regId;

    @Column(name = "UPD_DT")
    private LocalDateTime updDt;

    @Column(name = "UPD_ID")
    private String updId;

    // Getters & setters or Lombok annotations
}
