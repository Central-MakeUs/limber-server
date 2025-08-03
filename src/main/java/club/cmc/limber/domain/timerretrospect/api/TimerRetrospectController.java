package club.cmc.limber.domain.timerretrospect.api;

import club.cmc.limber.domain.timerretrospect.dto.TimerRetrospectRequestDto;
import club.cmc.limber.domain.timerretrospect.dto.TimerRetrospectResponseDto;
import club.cmc.limber.domain.timerretrospect.service.TimerRetrospectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timer-retrospects")
@Tag(name = "타이머 회고 API", description = "타이머 회고 등록 기능")
public class TimerRetrospectController {

    private final TimerRetrospectService timerRetrospectService;

    public TimerRetrospectController(TimerRetrospectService timerRetrospectService) {
        this.timerRetrospectService = timerRetrospectService;
    }

    @PostMapping
    @Operation(summary = "타이머 회고 저장")
    public ResponseEntity<TimerRetrospectResponseDto> saveRetrospect(
            @AuthenticationPrincipal String userId,
            @RequestBody TimerRetrospectRequestDto dto
    ) {
        return ResponseEntity.ok(timerRetrospectService.saveRetrospect(Long.valueOf(userId), dto));
    }
}
