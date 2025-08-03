package club.cmc.limber.domain.security.dto;


import lombok.Builder;

@Builder
public record TokenPair(String accessToken, String refreshToken) {}

