package br.dev.diisk.infra.services;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.application.services.IMessageService;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.presentation.dtos.response.ErrorDetailsResponse;
import br.dev.diisk.presentation.dtos.response.ErrorResponse;
import br.dev.diisk.presentation.dtos.response.PageResponse;
import br.dev.diisk.presentation.dtos.response.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseService implements IResponseService {

    private final IMessageService messageService;

    @Override
    public <T> ResponseEntity<SuccessResponse<T>> ok(T content) {
        return successResponse(content, HttpStatus.OK);
    }

    @Override
    public <T> ResponseEntity<SuccessResponse<T>> ok() {
        return ok(null);
    }

    @Override
    public <T> ResponseEntity<SuccessResponse<T>> created(URI uri, T content) {
        return ResponseEntity.created(uri).body(new SuccessResponse<T>(content, HttpStatus.CREATED.value()));
    }

    @Override
    public <T> ResponseEntity<SuccessResponse<T>> created(URI uri) {
        return created(uri, null);
    }

    @Override
    public <T> ResponseEntity<SuccessResponse<T>> created(T content) {
        return successResponse(content, HttpStatus.CREATED);
    }

    private <T> ResponseEntity<SuccessResponse<T>> successResponse(T content, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(
                new SuccessResponse<T>(content, httpStatus.value()));
    }

    private ResponseEntity<ErrorResponse> errorResponse(ErrorDetailsResponse error, HttpStatus httpStatus) {
        if (isMessagePath(error.getMessage()))
            error.setMessage(getMessageFromPath(error.getMessage()));
        ErrorResponse errorObject = ErrorResponse.getErrorInstance(httpStatus.value(), error);
        return ResponseEntity.status(httpStatus).body(errorObject);
    }

    private Boolean isMessagePath(String message) {
        return message.startsWith("{") && message.endsWith("}");
    }

    private String getMessageFromPath(String messagePath) {
        return messageService.getMessage(messagePath.substring(1, messagePath.length() - 1));
    }

    private ResponseEntity<ErrorResponse> errorResponse(String message,
            Collection<? extends ErrorDetailsResponse> errors, HttpStatus httpStatus) {
        if (isMessagePath(message))
            message = getMessageFromPath(message);

        errors = errors.stream().map((err) -> {
            if (isMessagePath(err.getMessage()))
                err.setMessage(getMessageFromPath(err.getMessage()));

            return err;
        }).toList();

        ErrorResponse errorObject = ErrorResponse.getErrorInstance(httpStatus.value(), message, errors);
        return ResponseEntity.status(httpStatus).body(errorObject);
    }

    @Override
    public ResponseEntity<ErrorResponse> badRequest(ErrorDetailsResponse error) {
        return errorResponse(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ErrorResponse> badRequest(String message, Collection<? extends ErrorDetailsResponse> errors) {
        return errorResponse(message, errors, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ErrorResponse> notFound(ErrorDetailsResponse error) {
        return errorResponse(error, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ErrorResponse> notFound(String message, Collection<? extends ErrorDetailsResponse> errors) {
        return errorResponse(message, errors, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ErrorResponse> unauthorized(ErrorDetailsResponse error) {
        return errorResponse(error, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<ErrorResponse> forbidden(ErrorDetailsResponse error) {
        return errorResponse(error, HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<ErrorResponse> internal(ErrorDetailsResponse error) {
        return errorResponse(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void writeResponseObject(HttpServletResponse response, Integer statusCode, Object responseObject)
            throws JsonProcessingException, IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseObject));
    }

    @Override
    public <S, T> PageResponse<T> getPageResponse(User user, Page<S> page, BaseMapper<S, T> mapper, String objectName) {

        PageResponse<T> response = new PageResponse<T>(
                page.getContent().stream()
                        .map(fe -> mapper.apply(user, fe))
                        .toList(),
                page.getTotalPages());

        return response;
    }

}
