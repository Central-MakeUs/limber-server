package club.cmc.limber.domain.timerhistory.api;


import club.cmc.limber.domain.timerhistory.dto.analytics.*;
import club.cmc.limber.domain.timerhistory.service.TimerHistoryAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "타이머 통계 API", description = """
        공통 조건:
        - 모든 API는 동일 파라미터(userId, startDate, endDate)를 요청 본문으로 받습니다.
        - (1)~(5): HistoryStatus = SENT, actualStartTime이 [startDate~endDate] 범위에 '포함'되는 데이터만 대상.
        - (6): HistoryStatus = FAILED, historyDt가 [startDate~endDate] 범위에 포함되는 데이터만 대상.
        계산 규칙:
        - '실험 시간' = actualStartTime~actualEndTime 구간 길이(분).
        - '예정 시간' = startTime~endTime 구간 길이(분).
        - '몰입도' = (실험 시간 합) / (예정 시간 합). 요일별은 요일 단위로 합산 후 계산.
        """)
@RestController
@RequestMapping("/api/timer-histories")
public class TimerHistoryAnalyticsController {

    private final TimerHistoryAnalyticsService service;

    public TimerHistoryAnalyticsController(TimerHistoryAnalyticsService service) {
        this.service = service;
    }

    // (1) 요일별 총 실험 시간
    @Operation(
            summary = "요일별 총 실험 시간",
            description = """
                    - 기준: SENT + actualStartTime이 기간에 포함되는 데이터.
                    - 응답: 요일 인덱스(일:0~토:6), DayOfWeek, 요일별 실제 실험 시간(분).
                    """,
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WeekdayActualDto.class))))
    )
    @PostMapping("/actual-by-weekday")
    public ResponseEntity<List<WeekdayActualDto>> actualByWeekday(@RequestBody RangeRequest req) {
        return ResponseEntity.ok(service.getActualByWeekday(req.userId(), req.startDate(), req.endDate()));
    }

    // (2) 요일별 총 몰입도
    @Operation(
            summary = "요일별 총 몰입도",
            description = """
                    - 기준: SENT + actualStartTime이 기간에 포함되는 데이터.
                    - 응답: 요일 인덱스/DayOfWeek, actual 총분, scheduled 총분, ratio(실제/예정).
                    """,
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WeekdayImmersionDto.class))))
    )
    @PostMapping("/immersion-by-weekday")
    public ResponseEntity<List<WeekdayImmersionDto>> immersionByWeekday(@RequestBody RangeRequest req) {
        return ResponseEntity.ok(service.getImmersionByWeekday(req.userId(), req.startDate(), req.endDate()));
    }

    // (3) 전체 총 실험 시간
    @Operation(
            summary = "전체 총 실험 시간",
            description = """
                    - 기준: SENT + actualStartTime이 기간에 포함되는 데이터.
                    - 응답: totalMinutes(분), label(사람 읽기 좋은 'X시간 Y분').
                    """,
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TotalActualDto.class)))
    )
    @PostMapping("/total-actual")
    public ResponseEntity<TotalActualDto> totalActual(@RequestBody RangeRequest req) {
        return ResponseEntity.ok(service.getTotalActual(req.userId(), req.startDate(), req.endDate()));
    }

    // (4) 전체 몰입도
    @Operation(
            summary = "전체 몰입도(달성률)",
            description = """
                    - 기준: SENT + actualStartTime이 기간에 포함되는 데이터.
                    - 응답: totalActualMinutes, totalScheduledMinutes, ratio(실제/예정).
                    """,
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TotalImmersionDto.class)))
    )
    @PostMapping("/total-immersion")
    public ResponseEntity<TotalImmersionDto> totalImmersion(
            @RequestBody RangeRequest req
    ) {
        return ResponseEntity.ok(service.getTotalImmersion(req.userId(), req.startDate(), req.endDate()));
    }

    // (5) RepeatCycleCode별 실제 시간 합
    @Operation(
            summary = "몰입 유형(RepeatCycleCode)별 실제 시간 합",
            description = """
                    - 기준: SENT + actualStartTime이 기간에 포함되는 데이터.
                    - 응답: RepeatCycleCode, totalActualMinutes(분).
                    """,
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FocusDistributionDto.class))))
    )
    @PostMapping("/focus-distribution")
    public ResponseEntity<List<FocusDistributionDto>> focusDistribution(
            @RequestBody RangeRequest req
    ) {
        return ResponseEntity.ok(service.getFocusDistribution(req.userId(), req.startDate(), req.endDate()));
    }

    // (6) 실패 사유 카운트
    @Operation(
            summary = "실패 사유별 카운트",
            description = """
                    - 기준: FAILED + historyDt가 기간에 포함되는 데이터.
                    - 응답: failReason 별 count.
                    """,
            responses = @ApiResponse(responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FailReasonCountDto.class))))
    )
    @PostMapping("/fail-reasons")
    public ResponseEntity<List<FailReasonCountDto>> failReasons(
            @RequestBody RangeRequest req
    ) {
        return ResponseEntity.ok(service.getFailReasonCounts(req.userId(), req.startDate(), req.endDate()));
    }
}
