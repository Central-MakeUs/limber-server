package club.cmc.limber.domain.focus.service;

import club.cmc.limber.domain.focus.dto.FocusTypeRequestDto;
import club.cmc.limber.domain.focus.dto.FocusTypeResponseDto;
import club.cmc.limber.domain.focus.entity.FocusType;

import java.util.List;

public interface FocusTypeService {
    FocusTypeResponseDto createFocusType(FocusTypeRequestDto dto);
    List<FocusTypeResponseDto> getFocusTypesByUserId(String userId);
    List<FocusType> getFilteredFocusTypes(String userId);
}
