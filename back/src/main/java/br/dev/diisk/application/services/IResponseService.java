package br.dev.diisk.application.services;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.dev.diisk.application.dtos.response.ErrorDetailsResponse;
import br.dev.diisk.application.dtos.response.ErrorResponse;
import br.dev.diisk.application.dtos.response.PageResponse;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.user.User;
import jakarta.servlet.http.HttpServletResponse;

public interface IResponseService {

    public <T> ResponseEntity<SuccessResponse<T>> ok(T content);

    public <T> ResponseEntity<SuccessResponse<T>> ok();

    public <T> ResponseEntity<SuccessResponse<T>> created(T content);

    public <T> ResponseEntity<SuccessResponse<T>> created(URI uri, T content);

    public <T> ResponseEntity<SuccessResponse<T>> created(URI uri);

    public ResponseEntity<ErrorResponse> badRequest(ErrorDetailsResponse error);

    public ResponseEntity<ErrorResponse> badRequest(String message, Collection<? extends ErrorDetailsResponse> errors);

    public ResponseEntity<ErrorResponse> notFound(ErrorDetailsResponse error);

    public ResponseEntity<ErrorResponse> notFound(String message, Collection<? extends ErrorDetailsResponse> errors);

    public ResponseEntity<ErrorResponse> unauthorized(ErrorDetailsResponse error);

    public ResponseEntity<ErrorResponse> forbidden(ErrorDetailsResponse error);

    public ResponseEntity<ErrorResponse> internal(ErrorDetailsResponse error);

    public <S, T> PageResponse<T> getPageResponse(User user, Page<S> page, BaseMapper<S, T> mapper, String objectName);

    public void writeResponseObject(HttpServletResponse response, Integer statusCode, Object responseObject)
            throws JsonProcessingException, IOException;

}
