package club.cmc.limber.domain.timerhistory.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체 몰입도")
public record TotalImmersionDto(
        @Schema(description = "전체 실제 시간 합(분)", example = "900") int totalActualMinutes,
        @Schema(description = "전체 예정 시간 합(분)", example = "1080") int totalScheduledMinutes,
        @Schema(description = "몰입도(실제/예정, 소수점 둘째 반올림)", example = "0.83") double ratio
) {}
