package club.cmc.limber.domain.timerhistory.dto.history;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "타이머 이력 기간 검색 요청 DTO (SearchRange 기준)")
public record TimerHistorySearchRequestDto(

        @Schema(description = "사용자 ID", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED)
        String userId,
        @Schema(description = "검색 주기", example = "ALL", requiredMode = Schema.RequiredMode.REQUIRED)
        SearchRange searchRange,           // ALL or WEEKLY
        @Schema(description = "검색 데이터 회고 여부", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        Boolean onlyIncompleteRetrospect   // true면 회고 미완료만, null/false면 전체

) {}
