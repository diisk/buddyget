package br.dev.diisk.application.dtos.response;

import org.springframework.validation.FieldError;

import br.dev.diisk.application.exceptions.FieldCustomRuntimeException;
import lombok.Getter;

@Getter
public class FieldErrorDetailsResponse extends ErrorDetailsResponse {
    private String field;

    public FieldErrorDetailsResponse(String field, String objectName, String message) {
        super(message, objectName);
        this.field = field;
    }

    public FieldErrorDetailsResponse(FieldError fieldError) {
        super(fieldError.getDefaultMessage(), fieldError.getObjectName());
        this.field = fieldError.getField();
    }

    public FieldErrorDetailsResponse(FieldCustomRuntimeException ex) {

        super(ex.getMessage(), ex.getClassObject() != null ? ex.getClassObject().getSimpleName() : null);
        this.field = ex.getField();
    }

}
