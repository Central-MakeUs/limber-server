package club.cmc.limber.domain.focus.dto;

public record FocusTypeResponseDto(
        Long id,
        String title,
        String userId,
        String defaultFlag,
        int sequence
) {}
