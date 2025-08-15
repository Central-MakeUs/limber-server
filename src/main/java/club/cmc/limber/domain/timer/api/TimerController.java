package club.cmc.limber.domain.timer.api;

import club.cmc.limber.common.response.CustomApiResponse;
import club.cmc.limber.domain.timer.dto.TimerDeleteDto;
import club.cmc.limber.domain.timer.dto.TimerRequestDto;
import club.cmc.limber.domain.timer.dto.TimerResponseDto;
import club.cmc.limber.domain.timer.dto.TimerStatusUpdateDto;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import club.cmc.limber.domain.timer.service.TimerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "타이머 API", description = "타이머 예약 및 상태 관리 기능 제공")
@RestController
@RequestMapping("/api/timers")
public class TimerController {

    private final TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @Operation(
            summary = "타이머 예약(즉시시작, 예약)",
            description = "타이머 지금 시작 혹은 예약합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<TimerResponseDto> createTimer(
            @RequestBody TimerRequestDto dto
    ) {
        return ResponseEntity.ok(timerService.createTimer(dto));
    }

    @Operation(summary = "유저 타이머 목록 조회")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TimerResponseDto>> getUserTimers(@PathVariable String userId) {
        return ResponseEntity.ok((timerService.getTimersByUserId(userId)));
    }

    @Operation(summary = "단일 타이머 조회")
    @GetMapping("/{timerId}")
    public ResponseEntity<TimerResponseDto> getTimerById(@PathVariable Long timerId) {
        return ResponseEntity.ok((timerService.getTimerById(timerId)));
    }

    @Operation(summary = "타이머 상태 변경")
    @PatchMapping("/{timerId}/status")
    public ResponseEntity<TimerResponseDto> updateTimerStatus(
            @PathVariable Long timerId,
            @RequestBody TimerStatusUpdateDto dto
    ) {
        return ResponseEntity.ok((timerService.updateTimerStatus(timerId, dto)));
    }

    @Operation(summary = "타이머 상태 조회")
    @GetMapping("/{timerId}/status")
    public ResponseEntity<TimerStatus> getTimerStatus(@PathVariable Long timerId) {
        return ResponseEntity.ok((timerService.getTimerStatus(timerId)));
    }

    @Operation(summary = "타이머 삭제")
    @DeleteMapping("/{timerId}")
    public ResponseEntity<Void> deleteTimer(@PathVariable Long timerId) {
        timerService.deleteTimer(timerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "타이머 삭제")
    @DeleteMapping()
    public ResponseEntity<Void> deleteTimer(
            @RequestBody TimerDeleteDto dto
    ) {
        timerService.deleteTimer(dto);
        return ResponseEntity.noContent().build();
    }
}
