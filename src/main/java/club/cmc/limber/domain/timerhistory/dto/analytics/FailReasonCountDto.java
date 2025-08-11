package club.cmc.limber.domain.timerhistory.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "실패 사유별 카운트")
public record FailReasonCountDto(
        @Schema(description = "실패 사유", example = "회의 겹침") String failReason,
        @Schema(description = "건수", example = "3") long count
) {}
