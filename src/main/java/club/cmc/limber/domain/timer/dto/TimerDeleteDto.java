package club.cmc.limber.domain.timer.dto;

import java.util.List;

public record TimerDeleteDto(
        List<Long> timerIds
) {}

