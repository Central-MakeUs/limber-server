package club.cmc.limber.common.response;


import club.cmc.limber.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    private CustomApiResponse(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> CustomApiResponse<T> success(T data) {
        return new CustomApiResponse<>(true, data, null);
    }

    public static CustomApiResponse<Void> success() {
        return new CustomApiResponse<>(true, null, null);
    }

    public static CustomApiResponse<Void> fail(ErrorCode code) {
        return new CustomApiResponse<>(false, null, new ErrorResponse(code));
    }

    public static CustomApiResponse<Void> fail(ErrorCode code, String customMessage) {
        return new CustomApiResponse<>(false, null, new ErrorResponse(code, customMessage));
    }

    @Getter
    public static class ErrorResponse {
        private final String code;
        private final String message;

        public ErrorResponse(ErrorCode code) {
            this.code = code.name();
            this.message = code.getMessage();
        }

        public ErrorResponse(ErrorCode code, String customMessage) {
            this.code = code.name();
            this.message = customMessage;
        }
    }
}
