package club.cmc.limber.common.exception;


import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 공통 오류
    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("입력값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // 사용자 정의 오류 예시
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("중복된 이메일입니다.", HttpStatus.CONFLICT),

    //
    NOT_FOUND_TIMER("타이머가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    TIMER_CONFLICT("해당 시간에 이미 진행 중인 타이머가 존재합니다.", HttpStatus.CONFLICT),
    TIMER_LIMIT_EXCEEDED("지금 시작은 최대 10개까지만 등록할 수 있습니다.", HttpStatus.BAD_REQUEST),
    NO_DELETE_TIMER_ID("삭제하는 타이머 ID가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),

    //
    NOT_FOUND_FOCUS_TYPE("집중유형이 존재하지 않습니다.", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
