package club.cmc.limber.domain.focus.service;


import club.cmc.limber.domain.focus.dto.FocusTypeRequestDto;
import club.cmc.limber.domain.focus.dto.FocusTypeResponseDto;
import club.cmc.limber.domain.focus.entity.FocusType;
import club.cmc.limber.domain.focus.repository.FocusTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FocusTypeServiceImpl implements FocusTypeService {

    private final FocusTypeRepository focusTypeRepository;

    @Override
    @Transactional
    public FocusTypeResponseDto createFocusType(FocusTypeRequestDto dto) {
        FocusType focusType = new FocusType();
        focusType.setUserId(dto.userId());
        focusType.setTitle(dto.title());
        focusType.setDefaultFlag("N");
        focusType.setDelFlag("N");
        focusType.setSequence(dto.sequence());
        focusType.setRegId("system");

        FocusType saved = focusTypeRepository.save(focusType);
        return toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FocusTypeResponseDto> getFocusTypesByUserId(String userId) {
        return focusTypeRepository.findByUserIdAndDelFlag(userId, "N")
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FocusType> getFilteredFocusTypes(String userId) {
        return focusTypeRepository.findCustomFocusTypes(userId);
    }

    private FocusTypeResponseDto toResponseDto(FocusType focusType) {
        return new FocusTypeResponseDto(
                focusType.getId(),
                focusType.getTitle(),
                focusType.getUserId(),
                focusType.getDefaultFlag(),
                focusType.getSequence()
        );
    }
}
