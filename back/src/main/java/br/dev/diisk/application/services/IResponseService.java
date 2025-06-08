package br.dev.diisk.application.services;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.ErrorTypeEnum;
import br.dev.diisk.presentation.dtos.response.ErrorResponse;
import br.dev.diisk.presentation.dtos.response.PageResponse;
import br.dev.diisk.presentation.dtos.response.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface IResponseService {

        public <T> ResponseEntity<SuccessResponse<T>> ok(T content);

        public <T> ResponseEntity<SuccessResponse<T>> ok();

        public ResponseEntity<ErrorResponse> error(ErrorTypeEnum type, String message, Map<String, String> details);

        public ResponseEntity<ErrorResponse> error(ErrorTypeEnum type, String message);

        public ResponseEntity<ErrorResponse> error(String message, ErrorTypeEnum type, String exceptionMessage);

        public <S, T> PageResponse<T> getPageResponse(User user, Page<S> page, Function<S, T> mapper);

        public void writeResponseObject(HttpServletResponse response, ErrorTypeEnum type, String message,
                        Map<String, String> details)
                        throws JsonProcessingException, IOException;

        public void writeResponseObject(HttpServletResponse response, ErrorTypeEnum type, String message)
                        throws JsonProcessingException, IOException;

}
