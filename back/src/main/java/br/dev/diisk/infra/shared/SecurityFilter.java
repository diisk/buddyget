package br.dev.diisk.infra.shared;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.application.shared.services.ITokenService;
import br.dev.diisk.domain.enums.ErrorTypeEnum;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final ITokenService tokenService;
    private final IUserRepository userRepository;
    private final IResponseService responseService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = recoverToken(request);
        if (token != null) {
            try {
                String subject = tokenService.getSubject(token);
                UserDetails user = userRepository.findById(Long.valueOf(subject)).orElse(null);

                if (user == null) {
                    writeInvalidUserOrToken(response);
                    return;
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JWTVerificationException ex) {
                writeInvalidUserOrToken(response);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    private void writeInvalidUserOrToken(HttpServletResponse response) throws JsonProcessingException, IOException {
        responseService.writeResponseObject(response, ErrorTypeEnum.DOMAIN_BUSINESS,
                "Usuario nao encontrado ou token invalida.");
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;

        return authHeader.replace("Bearer ", "");
    }

}
