package club.cmc.limber.domain.timer.entity;

import club.cmc.limber.domain.focus.entity.FocusType;
import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TITLE", nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOCUS_TYPE_ID", nullable = false)
    private FocusType focusType;

    @Enumerated(EnumType.STRING)
    @Column(name = "REPEAT_CYCLE_CODE", nullable = false, length = 20)
    private RepeatCycleCode repeatCycleCode;

    @Column(name = "REPEAT_DAYS", nullable = false, length = 20)
    private String repeatDays;

    @Column(name = "START_TIME", nullable = false, columnDefinition = "time(0)")
    private LocalTime startTime;

    @Column(name = "END_TIME", nullable = false, columnDefinition = "time(0)")
    private LocalTime endTime;

    @Column(name = "STATUS", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TimerStatus status;

    @Column(name = "DEL_FLAG", nullable = false, length = 1)
    private String delFlag = "N";

    @Column(name = "REG_DT", nullable = false)
    private LocalDateTime regDt;

    @Column(name = "REG_ID", nullable = false, length = 50)
    private String regId;

    @Column(name = "UPD_DT")
    private LocalDateTime updDt;

    @Column(name = "UPD_ID", length = 50)
    private String updId;
}
