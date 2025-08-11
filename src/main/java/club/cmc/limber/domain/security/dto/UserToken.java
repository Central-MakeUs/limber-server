package club.cmc.limber.domain.security.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "USER_TOKEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false, length = 255)
    private String userId;

    @Column(name = "REFRESH_TOKEN", nullable = false, columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "EXPIRES_AT", nullable = false)
    private ZonedDateTime expiresAt;

    @Column(name = "CREATED_AT", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "UPD_DT")
    private ZonedDateTime updatedAt;

    @Column(name = "UPD_ID", length = 50)
    private String updatedBy;
}
