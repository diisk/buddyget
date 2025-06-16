package br.dev.diisk.domain.shared.exceptions;

import java.util.Map;

import br.dev.diisk.domain.shared.enums.ErrorTypeEnum;
import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {

    private final Class<?> classObject;
    private final ErrorTypeEnum type;
    private final Map<String, String> details;

    protected DomainException(Class<?> classObject, ErrorTypeEnum type, String message,
            Map<String, String> details) {
        super(message);
        this.type = type;
        this.details = details == null ? Map.of() : details;
        this.classObject = classObject;
    }

}
