package br.dev.diisk.domain.exceptions;

import java.util.Map;

import br.dev.diisk.domain.enums.ErrorTypeEnum;

public class DatabaseValueNotFoundException extends DomainException {

    public DatabaseValueNotFoundException(String message, Class<?> classObject) {
        super(classObject, ErrorTypeEnum.NOT_FOUND, message, null);
    }

    public DatabaseValueNotFoundException(Class<?> classObject, String value) {
        super(classObject, ErrorTypeEnum.NOT_FOUND, "Valor não encontrado no banco de dados.",
                Map.of("valor", value));
    }

}
