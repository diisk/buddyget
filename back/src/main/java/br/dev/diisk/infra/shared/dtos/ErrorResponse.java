package br.dev.diisk.infra.shared.dtos;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends GenericResponse {

    private String message;
    private Map<String,String> details;

    public ErrorResponse(Integer statusCode, String message, Map<String,String> details) {
        super(statusCode, false);
        this.details = details;
        this.message = message;
    }

}
