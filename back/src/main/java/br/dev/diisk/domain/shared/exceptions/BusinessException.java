package br.dev.diisk.domain.shared.exceptions;

import java.util.Map;

import br.dev.diisk.domain.shared.ErrorTypeEnum;

public class BusinessException extends DomainException {

    public BusinessException(Class<?> classObject, String message, Map<String, String> details) {
        super(classObject, ErrorTypeEnum.DOMAIN_BUSINESS, message, details);
    }

    public BusinessException(Class<?> classObject, String message) {
        super(classObject, ErrorTypeEnum.DOMAIN_BUSINESS, message, null);
    }

}
