package br.dev.diisk.application.dtos.response;

import br.dev.diisk.application.exceptions.ValueCustomRuntimeException;
import lombok.Getter;

@Getter
public class ValueErrorDetailsResponse extends ErrorDetailsResponse {
    private String value;

    public ValueErrorDetailsResponse(String value, String objectName, String message) {
        super(message, objectName);
        this.value = value;
    }

    public ValueErrorDetailsResponse(ValueCustomRuntimeException ex) {
        super(ex.getMessage(), ex.getClassObject() != null ? ex.getClassObject().getSimpleName() : null);
        this.value = ex.getValue();
    }

}
