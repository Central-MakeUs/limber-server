package club.cmc.limber.domain.timerretrospect.service;

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
    public TimerRetrospectResponseDto saveRetrospect(Long userId, TimerRetrospectRequestDto dto) {
        TimerRetrospect saved = timerRetrospectRepository.save(dto.toEntity(userId));
        return TimerRetrospectResponseDto.from(saved);
    }
}

