package club.cmc.limber.domain.focus.dto;

public record FocusTypeRequestDto(
        Long userId,
        String title,
        int sequence
) {}
