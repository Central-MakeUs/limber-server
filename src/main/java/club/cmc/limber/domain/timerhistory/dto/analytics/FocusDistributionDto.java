package club.cmc.limber.domain.timerhistory.dto.analytics;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "반복 주기별 실제 시간 합(분)")
public record FocusDistributionDto(
        @Schema(description = "반복 주기 코드", example = "DAILY") RepeatCycleCode repeatCycleCode,
        @Schema(description = "해당 코드의 실제 시간 합(분)", example = "560") int totalActualMinutes
) {}
