package br.dev.diisk.presentation.dtos.response;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends GenericResponse {

    private String message;
    private Collection<? extends ErrorDetailsResponse> details;

    private ErrorResponse(Integer statusCode, String message, Collection<? extends ErrorDetailsResponse> details) {
        super(statusCode, false);
        this.details = details;
        this.message = message;
    }

    public static ErrorResponse getErrorInstance(Integer statusCode, String message) {
        return new ErrorResponse(statusCode, message, new ArrayList<>());
    }

    public static ErrorResponse getErrorInstance(Integer statusCode, String message,
            Collection<? extends ErrorDetailsResponse> details) {
        return new ErrorResponse(statusCode, message, details);
    }

    public static ErrorResponse getErrorInstance(Integer statusCode, String message, ErrorDetailsResponse[] details) {
        Collection<ErrorDetailsResponse> errors = new ArrayList<>();
        for (ErrorDetailsResponse detail : details) {
            errors.add(detail);
        }
        return new ErrorResponse(statusCode, message, errors);
    }

    public static ErrorResponse getErrorInstance(Integer statusCode, ErrorDetailsResponse detail) {
        return getErrorInstance(statusCode, detail.getMessage(), new ErrorDetailsResponse[] { detail });
    }

}
