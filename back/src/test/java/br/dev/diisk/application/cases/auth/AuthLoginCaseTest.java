package br.dev.diisk.application.cases.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.exceptions.authentication.InvalidUserException;
import br.dev.diisk.application.services.IAuthService;
import br.dev.diisk.application.services.ITokenService;
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
        String email = "john@example.com";
        String password = "wrongpassword";
        when(authService.authenticate(email, password)).thenThrow(new InvalidUserException(getClass()));
        // when(messageSource.getMessage("exception.invalid.user")).thenReturn("exception.invalid.user");

        // When & Then
        assertThrows(InvalidUserException.class, () -> {
            authLoginCase.execute(email,password);
        });
    }

    @Test
    public void authLoginCase_quandoCredenciaisValidas_DeveRetornarToken() {
        // Given
        String email = "john@example.com";
        String password = "password";
        User user = new User();
        when(authService.authenticate(email, password)).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn("valid-token");

        // When
        String token = authLoginCase.execute(email, password);

        // Then
        assertEquals("valid-token", token);
    }
}
