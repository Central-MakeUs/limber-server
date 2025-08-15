package club.cmc.limber.domain.focus.exception;

import club.cmc.limber.common.exception.ErrorCode;
import club.cmc.limber.common.response.BusinessException;

public class FocusTypeNotFoundException extends BusinessException {
    public FocusTypeNotFoundException() {
        super(ErrorCode.NOT_FOUND_FOCUS_TYPE);
    }
}
