package br.dev.diisk.infra.shared.handlers;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.shared.ErrorTypeEnum;
import br.dev.diisk.domain.shared.exceptions.DomainException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(2)
@RequiredArgsConstructor
public class GenericExceptionHandler {

    private final IResponseService responseService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) throws JsonProcessingException {
        // TODO: SALVAR LOG DO ERRO AQUI

        return responseService.error(null, "Erro interno do servidor. Tente novamente mais tarde.", null,
                ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleException(NoResourceFoundException ex) {
        return responseService.error(null, "Erro ao buscar recurso. O recurso solicitado não foi encontrado.",
                ErrorTypeEnum.DOMAIN_BUSINESS,
                ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleException(HttpMessageNotReadableException ex) {
        return responseService.error(null, "O corpo da requisição está diferente do esperado.",
                ErrorTypeEnum.DOMAIN_BUSINESS,
                ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleException(AuthorizationDeniedException ex) {
        return responseService.error(null, ErrorTypeEnum.UNAUTHORIZED,
                "Não autorizado. Você precisa estar autenticado para acessar este recurso.");
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleException(DomainException ex) {
        return responseService.error(ex.getClassObject(), ex.getType(), ex.getMessage(), ex.getDetails());
    }

}
