package br.dev.diisk.infra.shared.handlers;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.enums.ErrorTypeEnum;
import br.dev.diisk.domain.exceptions.DomainException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(2)
@RequiredArgsConstructor
public class GenericExceptionHandler {

    private final IResponseService responseService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) throws JsonProcessingException {
        // TODO: SALVAR LOG DO ERRO AQUI

        return responseService.error("Erro interno do servidor. Tente novamente mais tarde.", null, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleException(HttpMessageNotReadableException ex) throws JsonProcessingException {
        return responseService.error("O corpo da requisição está diferente do esperado.", ErrorTypeEnum.DOMAIN_BUSINESS,
                ex.getMessage());
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
