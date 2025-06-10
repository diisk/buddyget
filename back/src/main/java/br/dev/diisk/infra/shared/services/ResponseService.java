package br.dev.diisk.infra.shared.services;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.ErrorTypeEnum;
import br.dev.diisk.infra.shared.dtos.ErrorResponse;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseService implements IResponseService {

    @Value("${spring.profiles.active:dev}")
    private String env;

    private Integer getErrorCode(ErrorTypeEnum type) {
        if (type == null)
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        return switch (type) {
            case NOT_FOUND -> HttpServletResponse.SC_NOT_FOUND;
            case UNAUTHORIZED -> HttpServletResponse.SC_UNAUTHORIZED;
            case ACCESS_DENIED -> HttpServletResponse.SC_FORBIDDEN;
            case DOMAIN_BUSINESS -> HttpServletResponse.SC_BAD_REQUEST;
            case CONFLICT -> HttpServletResponse.SC_CONFLICT;
            default -> HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        };

    }

    @Override
    public <T> ResponseEntity<SuccessResponse<T>> ok(T content) {
        return successResponse(content, HttpStatus.OK);
    }

    @Override
    public <T> ResponseEntity<SuccessResponse<T>> ok() {
        return ok(null);
    }

    private <T> ResponseEntity<SuccessResponse<T>> successResponse(T content, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(
                new SuccessResponse<T>(content, httpStatus.value()));
    }

    @Override
    public <S, T> PageResponse<T> getPageResponse(User user, Page<S> page, Function<S,T> mapper) {

        PageResponse<T> response = new PageResponse<T>(
                page.getContent().stream()
                        .map(mapper)
                        .toList(),
                page.getTotalPages());

        return response;
    }

    @Override
    public ResponseEntity<ErrorResponse> error(ErrorTypeEnum type, String message, Map<String, String> details) {
        Integer errorCode = getErrorCode(type);
        return ResponseEntity.status(errorCode).body(
                new ErrorResponse(errorCode, message, details));
    }

    @Override
    public ResponseEntity<ErrorResponse> error(ErrorTypeEnum type, String message) {
        return error(type, message, null);
    }

    @Override
    public void writeResponseObject(HttpServletResponse response, ErrorTypeEnum type, String message,
            Map<String, String> details) throws JsonProcessingException, IOException {
        Integer errorCode = getErrorCode(type);
        response.setContentType("application/json");
        response.setStatus(errorCode);
        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(new ErrorResponse(errorCode, message, details)));
    }

    @Override
    public void writeResponseObject(HttpServletResponse response, ErrorTypeEnum type, String message)
            throws JsonProcessingException, IOException {
        writeResponseObject(response, type, message, null);
    }

    @Override
    public ResponseEntity<ErrorResponse> error(String message, ErrorTypeEnum type, String exceptionMessage) {
        if (env.equalsIgnoreCase("prod"))
            return error(type, message);

        return error(type, message, Map.of("exceptionMessage", exceptionMessage));

    }

}
