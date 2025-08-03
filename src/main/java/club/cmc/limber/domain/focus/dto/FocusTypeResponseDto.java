package club.cmc.limber.domain.focus.dto;

public record FocusTypeResponseDto(
        Long id,
        String title,
        Long userId,
        String defaultFlag,
        int sequence
) {}
