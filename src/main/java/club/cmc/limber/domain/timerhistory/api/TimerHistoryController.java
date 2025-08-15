package club.cmc.limber.domain.timerhistory.api;

import club.cmc.limber.common.response.CustomApiResponse;
import club.cmc.limber.domain.timerhistory.dto.history.*;
import club.cmc.limber.domain.timerhistory.service.TimerHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
            @Parameter(description = "사용자 ID", required = true)
            @RequestParam String userId,

            @Parameter(description = "검색 범위 (ALL|WEEKLY)", required = true)
            @RequestParam SearchRange searchRange,

            @Parameter(description = "회고 미완료만 필터링 여부", required = true)
            @RequestParam(required = false, defaultValue = "false") boolean onlyIncompleteRetrospect
    ) {
        TimerHistorySearchRequestDto req = new TimerHistorySearchRequestDto(
                userId,
                searchRange,
                onlyIncompleteRetrospect
        );

        return ResponseEntity.ok(timerHistoryService.searchWithRetrospect(req));
    }

    @Operation(
            summary = "타이머 이력 저장",
            description = "타이머 실행/알림 이력 1건을 저장합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "저장 요청 DTO",
                    content = @Content(schema = @Schema(implementation = TimerHistoryRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공",
                            content = @Content(schema = @Schema(implementation = TimerHistoryResponseDto.class)))
            }
    )
    @PostMapping
    public ResponseEntity<TimerHistoryResponseDto> saveHistory(
            @Valid @RequestBody TimerHistoryRequestDto dto
    ) {
        TimerHistoryResponseDto res = timerHistoryService.saveHistory(dto);
        return ResponseEntity
                .created(URI.create("/api/timer-histories/" + res.id()))
                .body(res);
    }

    @Operation(
            summary = "최신 타이머 이력 ID 조회",
            description = "userId와 timerId로 최신(historyDt 기준) 이력의 ID를 반환합니다. (delFlag='N'만 대상)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "204", description = "해당 조건의 이력이 없음")
            }
    )
    @GetMapping("/latest-id")
    public ResponseEntity<?> getLatestHistoryId(
            @Parameter(description = "사용자 ID", required = true) @RequestParam String userId,
            @Parameter(description = "타이머 ID", required = true) @RequestParam Long timerId
    ) {
        return timerHistoryService.findLatestHistoryId(userId, timerId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
