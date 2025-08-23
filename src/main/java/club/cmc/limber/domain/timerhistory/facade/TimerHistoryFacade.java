package club.cmc.limber.domain.timerhistory.facade;

import club.cmc.limber.domain.timerhistory.repository.TimerHistoryRepository;
import club.cmc.limber.domain.timerretrospect.repository.TimerRetrospectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimerHistoryFacade  {

    private final TimerHistoryRepository timerHistoryRepository;
    private final TimerRetrospectRepository timerRetrospectRepository;

    public TimerHistoryFacade(
            TimerHistoryRepository timerHistoryRepository,
            TimerRetrospectRepository timerRetrospectRepository
    ) {
        this.timerHistoryRepository = timerHistoryRepository;
        this.timerRetrospectRepository = timerRetrospectRepository;
    }

    @Transactional
    public void deleteUserHistoryAndRetrospect(String userId) {
        timerHistoryRepository.deleteAllByUserId(userId);
        timerRetrospectRepository.deleteAllByUserId(userId);
    }

}
