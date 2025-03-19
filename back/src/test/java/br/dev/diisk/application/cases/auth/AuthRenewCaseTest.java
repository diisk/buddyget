package br.dev.diisk.application.cases.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.services.ITokenService;
import br.dev.diisk.domain.entities.user.User;

public class AuthRenewCaseTest {

    @Mock
    private ITokenService tokenService;

    @InjectMocks
    private AuthRenewCase authRenewCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authRenewCase_quandoPossuiUsuario_deveRetornarNovoToken() {
        // Given
        User user = new User();
        when(tokenService.generateToken(user)).thenReturn("new-token");

        // When
        String token = authRenewCase.execute(user);

        // Then
        assertEquals("new-token", token);
    }
}
