package club.cmc.limber.domain.timerhistory.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;

@Schema(description = "요일별 실제 실험 시간(분)")
public record WeekdayActualDto(
        @Schema(description = "요일 인덱스 (일:0~토:6)", example = "1") int weekdayIndex,
        @Schema(description = "요일", example = "MONDAY") DayOfWeek dayOfWeek,
        @Schema(description = "해당 요일의 실제 실험 시간 합(분)", example = "210") int totalActualMinutes
) {}
