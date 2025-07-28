package club.cmc.limber.oauth.domain;


import lombok.Builder;

@Builder
public record TokenPair(String accessToken, String refreshToken) {}

