package club.cmc.limber.common.response;

import club.cmc.limber.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class}, basePackages = {"club.cmc.limber"})
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode code = ex.getErrorCode();
        log.warn("[BusinessException] [code={}] message={}", code.name(), ex.getMessage(), ex);
        return ResponseEntity
                .status(code.getStatus())
                .body(CustomApiResponse.fail(code, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("[ValidationException] [code={}] message={}", ErrorCode.INVALID_INPUT.name(), message, ex);

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(CustomApiResponse.fail(ErrorCode.INVALID_INPUT, message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("[TypeMismatchException] [code={}] param={} value={}",
                ErrorCode.INVALID_INPUT.name(), ex.getName(), ex.getValue(), ex);

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(CustomApiResponse.fail(ErrorCode.INVALID_INPUT, "Invalid type for input value."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<Void>> handleGeneralException(Exception ex) {
        log.error("[UnhandledException] [code={}] message={}", ErrorCode.INTERNAL_SERVER_ERROR.name(), ex.getMessage(), ex);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(CustomApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
