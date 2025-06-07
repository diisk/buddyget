package br.dev.diisk.infra.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.presentation.dtos.response.ErrorDetailsResponse;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(2)
@RequiredArgsConstructor
public class GenericExceptionHandler {

    private final IResponseService responseService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return responseService.internal(new ErrorDetailsResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleException(AuthorizationDeniedException ex) {
        return responseService.unauthorized(new ErrorDetailsResponse("{exception.access.denied}", null));
    }

}
