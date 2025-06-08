package br.dev.diisk.infra.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.enums.ErrorTypeEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class PersistenceExceptionHandler {

    private final IResponseService responseService;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return responseService.error("Entidade não encontrada.", ErrorTypeEnum.NOT_FOUND,
                ex.getMessage());
    }

}
