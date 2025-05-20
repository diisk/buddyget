package br.dev.diisk.infra.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.dev.diisk.application.dtos.response.ErrorDetailsResponse;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.exceptions.authentication.InvalidUserException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class AuthenticationExceptionHandler {

    private final IResponseService responseService;

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<?> handleInvalidUserException(InvalidUserException ex) {
        return responseService.badRequest(new ErrorDetailsResponse(ex.getMessage(), null));
    }

}
