package club.cmc.limber.domain.timerhistory.dto.analytics;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "반복 주기별 실제 시간 합(분)")
public record FocusDistributionDto(
        @Schema(description = "집중 유형 코드", example = "1") Long focusTypeId,
        @Schema(description = "집중 유형 코드명", example = "학습") String focusTypeName,
        @Schema(description = "해당 코드의 실제 시간 합(분)", example = "560") int totalActualMinutes
) {}
