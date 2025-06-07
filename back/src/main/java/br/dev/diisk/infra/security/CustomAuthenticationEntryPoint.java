package br.dev.diisk.infra.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final IResponseService responseService;
    private final IMessageService messageService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        Integer statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        ErrorResponse responseObject = ErrorResponse.getErrorInstance(statusCode, messageService.getMessage("exception.unauthorized"));
        responseService.writeResponseObject(response, statusCode, responseObject);
    }

}
