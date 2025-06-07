package br.dev.diisk.domain.exceptions;

import java.util.Map;

import br.dev.diisk.domain.enums.ErrorTypeEnum;

public class DatabaseValueConflictException extends DomainException {

    public DatabaseValueConflictException(String message, Class<?> classObject) {
        super(classObject, ErrorTypeEnum.CONFLICT, message, null);
    }

    public DatabaseValueConflictException(Class<?> classObject, String value) {
        super(classObject, ErrorTypeEnum.CONFLICT, "Valor já registrado no banco de dados.",
                Map.of("valor", value));
    }

}
