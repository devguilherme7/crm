package software.crm.infrastructure.exception;

import java.util.List;
import org.springframework.http.HttpStatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(int status, String message, List<ErrorDetails> errors) {

    public static ApiErrorResponse create(HttpStatusCode status, String message) {
        return new ApiErrorResponse(status.value(), message, null);
    }

    public static ApiErrorResponse create(HttpStatusCode status, String message, List<ErrorDetails> errors) {
        return new ApiErrorResponse(status.value(), message, errors);
    }

    public record ErrorDetails(String field, String message) {

    }
}
