package club.cmc.limber.infrastructure;

public class KakaoTokenResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    // getter, setter (혹은 lombok @Getter)

    public String getAccessToken() {
        return access_token;
    }
}

