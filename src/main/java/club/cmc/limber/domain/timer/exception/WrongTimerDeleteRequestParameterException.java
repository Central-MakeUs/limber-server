package club.cmc.limber.domain.timer.exception;

import club.cmc.limber.common.exception.ErrorCode;
import club.cmc.limber.common.response.BusinessException;

public class WrongTimerDeleteRequestParameterException extends BusinessException {
    public WrongTimerDeleteRequestParameterException() {
        super(ErrorCode.NO_DELETE_TIMER_ID);
    }
}
