package club.cmc.limber.domain.timerhistory.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;

@Schema(description = "요일별 몰입도")
public record WeekdayImmersionDto(
        @Schema(description = "요일 인덱스 (일:0~토:6)", example = "1") int weekdayIndex,
        @Schema(description = "요일", example = "MONDAY") DayOfWeek dayOfWeek,
        @Schema(description = "실제 시간 합(분)", example = "210") int totalActualMinutes,
        @Schema(description = "예정 시간 합(분)", example = "240") int totalScheduledMinutes,
        @Schema(description = "몰입도(실제/예정, 소수점 둘째 반올림)", example = "0.88") double ratio
) {}
