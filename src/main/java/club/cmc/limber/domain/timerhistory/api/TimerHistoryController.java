package club.cmc.limber.domain.timerhistory.api;

import club.cmc.limber.domain.timerhistory.dto.*;
import club.cmc.limber.domain.timerhistory.dto.analytics.TimerHistorySearchRequestDto;
import club.cmc.limber.domain.timerhistory.service.TimerHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "타이머 이력 API", description = "타이머 히스토리(알림 전송 등) 관련 API")
@RestController
@RequestMapping("/api/timer-histories")
public class TimerHistoryController {

    private final TimerHistoryService timerHistoryService;

    public TimerHistoryController(TimerHistoryService timerHistoryService) {
        this.timerHistoryService = timerHistoryService;
    }

    @Operation(
            summary = "이력 조회 (SENT / actual 기간 겹침)",
            description = "요청한 날짜 범위와 실제 수행 구간(actualStart~actualEnd)이 겹치는 SENT 데이터를 반환합니다.",
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimerHistoryResponseDto.class))))
    )
    @GetMapping("/me")
    public ResponseEntity<List<TimerHistoryResponseDto>> getHistoriesByUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "userId",
                    content = @Content(schema = @Schema(implementation = TimerHistorySearchRequestDto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody TimerHistorySearchRequestDto searchRequestDto
    ) {
        return ResponseEntity.ok(timerHistoryService.getHistoriesByUserId(searchRequestDto.userId()));
    }

}
