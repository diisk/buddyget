package br.dev.diisk.web.middlewares.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.dtos.response.ErrorResponse;
import br.dev.diisk.application.interfaces.IResponseService;
import br.dev.diisk.domain.MessageUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final IResponseService responseService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        Integer statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        ErrorResponse responseObject = ErrorResponse.getErrorInstance(statusCode, MessageUtils.getMessage("exception.unauthorized"));
        responseService.writeResponseObject(response, statusCode, responseObject);
    }

}
