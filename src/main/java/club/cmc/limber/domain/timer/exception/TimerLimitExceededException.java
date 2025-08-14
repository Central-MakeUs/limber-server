package club.cmc.limber.domain.timer.exception;

import club.cmc.limber.common.exception.ErrorCode;
import club.cmc.limber.common.response.BusinessException;

public class TimerLimitExceededException extends BusinessException {
    public TimerLimitExceededException() {
        super(ErrorCode.TIMER_LIMIT_EXCEEDED);
    }
}
