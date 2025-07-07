package club.cmc.limber.response;

import club.cmc.limber.exception.ErrorCode;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public BusinessException(ErrorCode code, String customMessage) {
        super(customMessage);
        this.errorCode = code;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
