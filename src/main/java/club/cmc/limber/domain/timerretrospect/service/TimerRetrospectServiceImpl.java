package club.cmc.limber.domain.timerretrospect.service;

import club.cmc.limber.domain.focus.dto.FocusTypeResponseDto;
import club.cmc.limber.domain.focus.entity.FocusType;
import club.cmc.limber.domain.timer.entity.Timer;
import club.cmc.limber.domain.timer.enums.TimerStatus;
import club.cmc.limber.domain.timerretrospect.dto.TimerRetrospectRequestDto;
import club.cmc.limber.domain.timerretrospect.dto.TimerRetrospectResponseDto;
import club.cmc.limber.domain.timerretrospect.entity.TimerRetrospect;
import club.cmc.limber.domain.timerretrospect.repository.TimerRetrospectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimerRetrospectServiceImpl implements TimerRetrospectService {

    private final TimerRetrospectRepository timerRetrospectRepository;

    public TimerRetrospectServiceImpl(TimerRetrospectRepository timerRetrospectRepository) {
        this.timerRetrospectRepository = timerRetrospectRepository;
    }

    @Override
    @Transactional
    public TimerRetrospectResponseDto saveRetrospect(TimerRetrospectRequestDto dto) {
        TimerRetrospect saved = timerRetrospectRepository.save(dto.toEntity());
        return TimerRetrospectResponseDto.from(saved);
    }

    @Override
    public TimerRetrospectResponseDto deleteTimerRetrospect(Long timerRetrospectId) {
        TimerRetrospect timerRetrospect = timerRetrospectRepository.findById(timerRetrospectId)
                .orElseThrow(() -> new IllegalArgumentException("타이머가 존재하지 않습니다."));

        timerRetrospect.setDelFlag("Y");
        return toResponseDto(timerRetrospect);
    }

    private TimerRetrospectResponseDto toResponseDto(TimerRetrospect timerRetrospect) {
        return new TimerRetrospectResponseDto(
                timerRetrospect.getId(),
                timerRetrospect.getTimerHistoryId(),
                timerRetrospect.getTimerId(),
                timerRetrospect.getUserId(),
                timerRetrospect.getImmersion(),
                timerRetrospect.getComment(),
                timerRetrospect.getDelFlag()
        );
    }
}

