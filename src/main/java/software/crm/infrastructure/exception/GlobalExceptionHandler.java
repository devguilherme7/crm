package software.crm.infrastructure.exception;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@SuppressWarnings("NullableProblems")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<ApiErrorResponse.ErrorDetails> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ApiErrorResponse.ErrorDetails(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();

        ApiErrorResponse response = ApiErrorResponse.create(status,
                "A requisição contém dados inválidos. Por favor, revise todos os campos.", errors);
        return ResponseEntity.status(status).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiErrorResponse response = ApiErrorResponse.create(status,
                "O corpo da requisição está malformado ou ilegível.");

        return ResponseEntity.status(status).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiErrorResponse response = ApiErrorResponse.create(status,
                "O método HTTP utilizado não é suportado para este endpoint.");

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        if (ex instanceof ApiException apiException) {
            return ResponseEntity.status(apiException.getStatusCode()).body(
                    ApiErrorResponse.create(apiException.getStatusCode(), apiException.getMessage()));
        }

        LOG.error("Unhandled exception", ex);
        ApiErrorResponse response = ApiErrorResponse.create(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno no servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
