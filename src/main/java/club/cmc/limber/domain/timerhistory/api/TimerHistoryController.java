package club.cmc.limber.domain.timerhistory.api;

import club.cmc.limber.domain.focus.dto.FocusTypeRequestDto;
import club.cmc.limber.domain.focus.dto.FocusTypeResponseDto;
import club.cmc.limber.domain.focus.service.FocusTypeService;
import club.cmc.limber.domain.timerhistory.dto.TimerHistoryResponseDto;
import club.cmc.limber.domain.timerhistory.service.TimerHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "타이머 이력 API", description = "타이머 히스토리(알림 전송 등) 관련 API")
@RestController
@RequestMapping("/api/timer-histories")
public class TimerHistoryController {

    private final TimerHistoryService timerHistoryService;

    public TimerHistoryController(TimerHistoryService timerHistoryService) {
        this.timerHistoryService = timerHistoryService;
    }

    @Operation(summary = "사용자별 타이머 이력 조회")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TimerHistoryResponseDto>> getHistoriesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(timerHistoryService.getHistoriesByUserId(userId));
    }
}
