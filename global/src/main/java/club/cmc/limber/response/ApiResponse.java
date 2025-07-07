package club.cmc.limber.response;


import club.cmc.limber.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    private ApiResponse(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, null);
    }

    public static ApiResponse<Void> fail(ErrorCode code) {
        return new ApiResponse<>(false, null, new ErrorResponse(code));
    }

    public static ApiResponse<Void> fail(ErrorCode code, String customMessage) {
        return new ApiResponse<>(false, null, new ErrorResponse(code, customMessage));
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
