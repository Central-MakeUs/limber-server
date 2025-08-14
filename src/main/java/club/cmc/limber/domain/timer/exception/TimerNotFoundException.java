package club.cmc.limber.domain.timer.exception;

import club.cmc.limber.common.exception.ErrorCode;
import club.cmc.limber.common.response.BusinessException;

public class TimerNotFoundException extends BusinessException {
    public TimerNotFoundException() {
        super(ErrorCode.NOT_FOUND_TIMER);
    }
}
