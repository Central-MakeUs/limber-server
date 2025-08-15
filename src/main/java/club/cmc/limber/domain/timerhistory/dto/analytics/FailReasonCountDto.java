package club.cmc.limber.domain.timerhistory.dto.analytics;

import club.cmc.limber.domain.timerhistory.enums.FailReason;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "실패 사유별 카운트")
public record FailReasonCountDto(
        @Schema(description = "실패 사유", example = "LACK_OF_FOCUS_INTENTION") FailReason failReason,
        @Schema(description = "건수", example = "3") long count
) {}
