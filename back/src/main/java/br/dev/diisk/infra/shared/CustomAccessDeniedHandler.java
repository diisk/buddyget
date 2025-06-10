package br.dev.diisk.infra.shared;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.enums.ErrorTypeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final IResponseService responseService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        responseService.writeResponseObject(response, ErrorTypeEnum.ACCESS_DENIED,
                "Acesso negado. Você não tem permissão para acessar este recurso.");
    }

}
