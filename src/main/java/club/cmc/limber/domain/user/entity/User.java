package club.cmc.limber.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;  // ID

    @Column(name = "USER_ID", length = 255)
    private String userId;  // ID

    @Column(name = "EMAIL", length = 255)
    private String email;  // 이메일

    @Column(name = "LOGIN_TYPE", length = 20)
    private String loginType;  // 로그인 유형 (KAKAO, NAVER, GOOGLE 등)

    @Column(name = "OAUTH_ID", length = 100)
    private String oauthId;  // 소셜 계정 고유 식별자 (sub 등)

    @Column(name = "NICKNAME", length = 100)
    private String nickname;  // 닉네임

    @Column(name = "FCM_KEY", length = 255)
    private String fcmKey;  // FCM KEY (푸시 알림용)

    @CreationTimestamp
    @Column(name = "REG_DT", nullable = false)
    private LocalDateTime regDt;  // 등록시간

    @Column(name = "REG_ID", length = 50, nullable = false)
    private String regId;  // 등록자

    @UpdateTimestamp
    @Column(name = "UPD_DT")
    private LocalDateTime updDt;  // 수정시간

    @Column(name = "UPD_ID", length = 50)
    private String updId;  // 수정자
}
