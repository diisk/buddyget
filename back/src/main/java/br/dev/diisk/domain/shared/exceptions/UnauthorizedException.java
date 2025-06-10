package br.dev.diisk.domain.shared.exceptions;

import br.dev.diisk.domain.shared.ErrorTypeEnum;

public class UnauthorizedException extends DomainException {

    public UnauthorizedException(Class<?> classObject) {
        super(classObject, ErrorTypeEnum.UNAUTHORIZED,
                "Não autorizado. Você precisa estar autenticado para acessar este recurso.", null);
    }

    public UnauthorizedException(Class<?> classObject, String message) {
        super(classObject, ErrorTypeEnum.UNAUTHORIZED, message, null);
    }

}
