package br.dev.diisk.domain.shared.exceptions;

import java.util.Map;

import br.dev.diisk.domain.shared.enums.ErrorTypeEnum;

public class NullOrEmptyException extends DomainException {

    public NullOrEmptyException(String message, Class<?> classObject) {
        super(classObject, ErrorTypeEnum.DOMAIN_BUSINESS, message, null);
    }

    public NullOrEmptyException(Class<?> classObject, String fieldName) {
        super(classObject, ErrorTypeEnum.DOMAIN_BUSINESS, "O campo não pode ser nulo ou vazio.",
                Map.of("campo", fieldName));
    }

}
