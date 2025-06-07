package br.dev.diisk.domain.exceptions;

import java.util.Map;

import br.dev.diisk.domain.enums.ErrorTypeEnum;

public class BusinessException extends DomainException {

    public BusinessException(Class<?> classObject, String message, Map<String, String> details) {
        super(classObject, ErrorTypeEnum.DOMAIN_BUSINESS, message, details);
    }

}
