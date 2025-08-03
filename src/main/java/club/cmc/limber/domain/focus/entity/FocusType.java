package club.cmc.limber.domain.focus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "FOCUS_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FocusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "DEFAULT_FLAG", nullable = false, length = 1)
    private String defaultFlag = "N";

    @Column(name = "DEL_FLAG", nullable = false, length = 1)
    private String delFlag = "N";

    @Column(name = "SEQUENCE", nullable = false)
    private int sequence = 0;

    @Column(name = "REG_DT", nullable = false)
    private LocalDateTime regDt;

    @Column(name = "REG_ID", nullable = false, length = 50)
    private String regId;

    @Column(name = "UPD_DT")
    private LocalDateTime updDt;

    @Column(name = "UPD_ID", length = 50)
    private String updId;
}
