package br.dev.diisk.infra.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.services.IMessageService;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.presentation.dtos.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final IResponseService responseService;
    private final IMessageService messageService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Integer statusCode = HttpServletResponse.SC_FORBIDDEN;
        ErrorResponse responseObject = ErrorResponse.getErrorInstance(statusCode,
                messageService.getMessage("exception.access.denied"));
        responseService.writeResponseObject(response, statusCode, responseObject);
    }

}
