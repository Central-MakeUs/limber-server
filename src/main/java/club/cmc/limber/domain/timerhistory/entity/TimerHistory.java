package club.cmc.limber.domain.timerhistory.entity;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "TIMER_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIMER_ID", nullable = false)
    private Long timerId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "FOCUS_TYPE_ID", nullable = false)
    private Long focusTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "REPEAT_CYCLE_CODE", nullable = false)
    private RepeatCycleCode repeatCycleCode;

    @Column(name = "REPEAT_DAYS", nullable = false, length = 20)
    private String repeatDays;

    @Column(name = "HISTORY_DT", nullable = false)
    private LocalDateTime historyDt;

    @Enumerated(EnumType.STRING)
    @Column(name = "HISTORY_STATUS", nullable = false)
    private HistoryStatus historyStatus;

    @Column(name = "FAIL_REASON")
    private String failReason;

    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private LocalTime endTime;

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
}
