package br.dev.diisk.application.cases.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.dtos.auth.LoginRequest;
import br.dev.diisk.application.exceptions.authentication.InvalidUserException;
import br.dev.diisk.application.interfaces.auth.IAuthService;
import br.dev.diisk.application.interfaces.auth.ITokenService;
import br.dev.diisk.domain.entities.user.User;

public class AuthLoginCaseTest {

    @Mock
    private IAuthService authService;

    @Mock
    private ITokenService tokenService;

    @InjectMocks
    private AuthLoginCase authLoginCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authLoginCase_quandoCredenciaisInvalidas_DeveLancarExcecao() {
        // Given
        LoginRequest request = new LoginRequest("john@example.com", "wrongpassword");
        when(authService.authenticate(request.getEmail(), request.getPassword())).thenThrow(new InvalidUserException(getClass()));

        // When & Then
        assertThrows(InvalidUserException.class, () -> {
            authLoginCase.execute(request);
        });
    }

    @Test
    public void authLoginCase_quandoCredenciaisValidas_DeveRetornarToken() {
        // Given
        LoginRequest request = new LoginRequest("john@example.com", "password");
        User user = new User();
        when(authService.authenticate(request.getEmail(), request.getPassword())).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn("valid-token");

        // When
        String token = authLoginCase.execute(request);

        // Then
        assertEquals("valid-token", token);
    }
}
