package club.cmc.limber.domain.focus.api;

import club.cmc.limber.domain.focus.dto.FocusTypeRequestDto;
import club.cmc.limber.domain.focus.dto.FocusTypeResponseDto;
import club.cmc.limber.domain.focus.entity.FocusType;
import club.cmc.limber.domain.focus.service.FocusTypeService;
import club.cmc.limber.domain.security.dto.TokenPair;
import club.cmc.limber.domain.security.dto.TokenRequest;
import club.cmc.limber.domain.security.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "집중유형 API", description = "FocusType 등록 및 조회 기능 제공")
@RestController
@RequestMapping("/api/focus-types")
public class FocusTypeController {

    private final FocusTypeService focusTypeService;

    public FocusTypeController(FocusTypeService focusTypeService) {
        this.focusTypeService = focusTypeService;
    }

    @Operation(summary = "집중유형 등록")
    @PostMapping
    public ResponseEntity<FocusTypeResponseDto> createFocusType(@RequestBody FocusTypeRequestDto dto) {
        return ResponseEntity.ok(focusTypeService.createFocusType(dto));
    }

    @Operation(summary = "유저의 집중유형 목록 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<List<FocusType>> getFocusTypes(@PathVariable String userId) {
        List<FocusType> results = focusTypeService.getFilteredFocusTypes(userId);
        return ResponseEntity.ok(results);
    }
}

