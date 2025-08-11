package club.cmc.limber.domain.timerhistory.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RangeRequest(
        @Schema(description = "사용자 ID", example = "user123") String userId,
        @Schema(description = "검색 시작 날짜(yyyy-MM-dd)", example = "2025-08-01") LocalDate startDate,
        @Schema(description = "검색 종료 날짜(yyyy-MM-dd)", example = "2025-08-07") LocalDate endDate
) {}
