package club.cmc.limber.domain.timerhistory.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "타이머 이력 기간 검색 요청 DTO (actualStartTime ~ actualEndTime 기준)")
public record TimerHistorySearchRequestDto(

        @Schema(description = "사용자 ID", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED)
        String userId,

        @Schema(description = "검색 시작 날짜", example = "2025-08-01", requiredMode = Schema.RequiredMode.REQUIRED)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @Schema(description = "검색 종료 날짜", example = "2025-08-07", requiredMode = Schema.RequiredMode.REQUIRED)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) {}
