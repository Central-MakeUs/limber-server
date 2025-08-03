package club.cmc.limber.domain.focus.dto;

public record FocusTypeRequestDto(
        Long userId,
        String title,
        String defaultFlag,
        int sequence
) {}
