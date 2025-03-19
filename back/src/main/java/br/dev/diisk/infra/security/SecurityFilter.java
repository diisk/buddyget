package br.dev.diisk.infra.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.dev.diisk.application.dtos.response.ErrorResponse;
import br.dev.diisk.application.services.IMessageService;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.application.services.ITokenService;
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
    private final IMessageService messageService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = recoverToken(request);
        if (token != null) {
            try {
                String subject = tokenService.validateToken(token);
                UserDetails user = userRepository.findByEmail(subject).orElse(null);

                if (user == null) {
                    Integer statusCode = HttpServletResponse.SC_BAD_REQUEST;
                    ErrorResponse responseObject = ErrorResponse.getErrorInstance(statusCode,
                            messageService.getMessage("exception.invalid.token"));
                    responseService.writeResponseObject(response, statusCode, responseObject);
                    return;
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JWTVerificationException ex) {
                ex.printStackTrace();
            }

        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;

        return authHeader.replace("Bearer ", "");
    }

}
