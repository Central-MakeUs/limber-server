package club.cmc.limber.domain.timerhistory.api;

import club.cmc.limber.common.response.CustomApiResponse;
import club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryResponseDto;
import club.cmc.limber.domain.timerhistory.dto.history.TimerHistorySearchRequestDto;
import club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryWeeklyGroupDto;
import club.cmc.limber.domain.timerhistory.dto.history.TimerHistoryWithRetrospectDto;
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
            description = "기존 API (참고용)",
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

    @Operation(
            summary = "이력 검색 (searchRange만 사용, 회고 미완료 필터 가능)",
            description = """
                    - 기간 파라미터 없음: 전체 이력 대상
                    - 대상: SENT 상태 & delFlag='N'
                    - onlyIncompleteRetrospect=true면 회고 미완료만
                    - searchRange=ALL → flat list
                    - searchRange=WEEKLY → 주 단위(월~일) 그룹 리스트
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "ALL일 때 리스트 반환",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimerHistoryWithRetrospectDto.class)))),
                    @ApiResponse(responseCode = "200", description = "WEEKLY일 때 주차 그룹 리스트 반환",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimerHistoryWeeklyGroupDto.class))))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "userId, searchRange(ALL|WEEKLY), onlyIncompleteRetrospect",
                    content = @Content(schema = @Schema(implementation = TimerHistorySearchRequestDto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody TimerHistorySearchRequestDto req
    ) {
        return ResponseEntity.ok(timerHistoryService.searchWithRetrospect(req));
    }

}
