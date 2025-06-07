package br.dev.diisk.infra.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.enums.ErrorTypeEnum;
import br.dev.diisk.domain.exceptions.DomainException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(2)
@RequiredArgsConstructor
public class GenericExceptionHandler {

    private final IResponseService responseService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        // TODO: SALVAR LOG DO ERRO AQUI
        return responseService.error(null, ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleException(AuthorizationDeniedException ex) {
        return responseService.error(ErrorTypeEnum.UNAUTHORIZED,
                "Não autorizado. Você precisa estar autenticado para acessar este recurso.");
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleException(DomainException ex) {
        return responseService.error(ex.getType(), ex.getMessage(), ex.getDetails());
    }

}
