package club.cmc.limber.domain.timerhistory.dto.analytics;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체 총 실험 시간")
public record TotalActualDto(
        @Schema(description = "총 실험 시간(분)", example = "1234") int totalMinutes,
        @Schema(description = "사람이 읽기 좋은 라벨", example = "20시간 34분") String label
) {}
