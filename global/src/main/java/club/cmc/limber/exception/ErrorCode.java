package club.cmc.limber.exception;


import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 공통 오류
    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("입력값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // 사용자 정의 오류 예시
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("중복된 이메일입니다.", HttpStatus.CONFLICT);

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
