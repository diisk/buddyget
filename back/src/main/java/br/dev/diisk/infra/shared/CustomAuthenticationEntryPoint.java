package br.dev.diisk.infra.shared;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.shared.enums.ErrorTypeEnum;
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

        responseService.writeResponseObject(response, ErrorTypeEnum.UNAUTHORIZED,
                "Não autorizado. Você precisa estar autenticado para acessar este recurso.");
    }

}
