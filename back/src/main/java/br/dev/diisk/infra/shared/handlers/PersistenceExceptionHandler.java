package br.dev.diisk.infra.shared.handlers;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.shared.enums.ErrorTypeEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class PersistenceExceptionHandler {

    private final IResponseService responseService;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return responseService.error(null, "Entidade não encontrada.", ErrorTypeEnum.NOT_FOUND,
                ex.getMessage());
    }

}
