package br.dev.diisk.infra.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.dev.diisk.application.dtos.response.ErrorDetailsResponse;
import br.dev.diisk.application.services.IResponseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class PersistenceExceptionHandler {

    private final IResponseService responseService;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return responseService.notFound(new ErrorDetailsResponse(ex.getMessage(), null));
    }

}
