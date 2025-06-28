package br.dev.diisk.application.user.cases;

import br.dev.diisk.application.shared.services.IAuthService;
import br.dev.diisk.application.shared.services.ITokenService;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para LoginUserCase.
 * Segue padrão Given-When-Then e cobre cenários felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class LoginUserCaseTest {

    @Mock
    private IAuthService authService;
    @Mock
    private ITokenService tokenService;
    @Mock
    private User user;

    @InjectMocks
    private LoginUserCase loginUserCase;

    // Caminho feliz: autenticação e geração de token com sucesso
    @Test
    void loginUserCase_deveRetornarToken_quandoCredenciaisValidas() {
        // Given
        String email = "user@email.com";
        String password = "senha123";
        Long userId = 1L;
        String token = "tokenGerado";
        when(authService.authenticate(email, password)).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(tokenService.generateToken(userId.toString())).thenReturn(token);

        // When
        String result = loginUserCase.execute(email, password);

        // Then
        assertEquals(token, result);
        verify(authService).authenticate(email, password);
        verify(tokenService).generateToken(userId.toString());
    }

    // Exceção: email nulo
    @Test
    void loginUserCase_deveLancarExcecao_quandoEmailNulo() {
        // Given
        String email = null;
        String password = "senha123";

        // When / Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> loginUserCase.execute(email, password));
        assertEquals("email", ex.getDetails().get("campo"));
    }

    // Exceção: email vazio
    @Test
    void loginUserCase_deveLancarExcecao_quandoEmailVazio() {
        // Given
        String email = "   ";
        String password = "senha123";

        // When / Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> loginUserCase.execute(email, password));
        assertEquals("email", ex.getDetails().get("campo"));
    }

    // Exceção: senha nula
    @Test
    void loginUserCase_deveLancarExcecao_quandoSenhaNula() {
        // Given
        String email = "user@email.com";
        String password = null;

        // When / Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> loginUserCase.execute(email, password));
        assertEquals("password", ex.getDetails().get("campo"));
    }

    // Exceção: senha vazia
    @Test
    void loginUserCase_deveLancarExcecao_quandoSenhaVazia() {
        // Given
        String email = "user@email.com";
        String password = "   ";

        // When / Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> loginUserCase.execute(email, password));
        assertEquals("password", ex.getDetails().get("campo"));
    }
}
