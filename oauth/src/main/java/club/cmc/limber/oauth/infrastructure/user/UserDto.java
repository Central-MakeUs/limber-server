package club.cmc.limber.oauth.infrastructure.user;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String nickname;
}
