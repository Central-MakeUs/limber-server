package club.cmc.limber.domain.timer.exception;

import club.cmc.limber.common.exception.ErrorCode;
import club.cmc.limber.common.response.BusinessException;

public class TimerConflictException extends BusinessException {

    public TimerConflictException() {
        super(ErrorCode.TIMER_CONFLICT);
    }
}
