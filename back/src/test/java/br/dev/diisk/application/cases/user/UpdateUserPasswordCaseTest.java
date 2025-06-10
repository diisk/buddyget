package br.dev.diisk.application.cases.user;

import br.dev.diisk.application.shared.services.ISecurityService;
import br.dev.diisk.application.user.cases.UpdateUserPasswordCase;
import br.dev.diisk.application.user.dtos.UpdateUserPasswordParams;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.exceptions.UnauthorizedException;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import br.dev.diisk.domain.value_objects.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso UpdateUserPasswordCase.
 * Cobre cenários felizes, limites e de exceção.
 */
@ExtendWith(MockitoExtension.class)
class UpdateUserPasswordCaseTest {
    @Mock
    private IUserRepository userRepository;
    @Mock
    private ISecurityService securityService;
    @InjectMocks
    private UpdateUserPasswordCase updateUserPasswordCase;

    private User user;
    private final String encryptedPassword = "senhaCriptografada";

    @BeforeEach
    void setUp() {
        user = Mockito.mock(User.class);
    }

    /**
     * Deve atualizar a senha do usuário quando os dados forem válidos.
     */
    @Test
    void updateUserPasswordCase_deveAtualizarSenha_quandoDadosValidos() {
        // Given
        UpdateUserPasswordParams params = new UpdateUserPasswordParams("NovaSenha@123", "SenhaAtual@123");
        when(user.getEncryptedPassword()).thenReturn(encryptedPassword);
        when(securityService.matchPasswords("SenhaAtual@123", encryptedPassword)).thenReturn(true);
        when(securityService.encryptPassword(any(Password.class))).thenReturn("NovaSenhaCriptografada");

        // When
        updateUserPasswordCase.execute(user, params);

        // Then
        verify(user).update(null, "NovaSenhaCriptografada");
        verify(userRepository).save(user);
    }

    /**
     * Deve lançar exceção se a senha atual for nula ou vazia.
     */
    @Test
    void updateUserPasswordCase_deveLancarExcecao_quandoSenhaAtualNulaOuVazia() {
        // Given
        UpdateUserPasswordParams paramsNula = new UpdateUserPasswordParams("NovaSenha@123", null);
        UpdateUserPasswordParams paramsVazia = new UpdateUserPasswordParams("NovaSenha@123", "");

        // When/Then
        NullOrEmptyException ex1 = assertThrows(NullOrEmptyException.class, () -> updateUserPasswordCase.execute(user, paramsNula));
        assertEquals("password", ex1.getDetails().get("campo"));
        NullOrEmptyException ex2 = assertThrows(NullOrEmptyException.class, () -> updateUserPasswordCase.execute(user, paramsVazia));
        assertEquals("password", ex2.getDetails().get("campo"));
    }

    /**
     * Deve lançar exceção se a nova senha for nula ou vazia.
     */
    @Test
    void updateUserPasswordCase_deveLancarExcecao_quandoNovaSenhaNulaOuVazia() {
        // Given
        UpdateUserPasswordParams paramsNula = new UpdateUserPasswordParams(null, "SenhaAtual@123");
        UpdateUserPasswordParams paramsVazia = new UpdateUserPasswordParams("", "SenhaAtual@123");

        // When/Then
        NullOrEmptyException ex1 = assertThrows(NullOrEmptyException.class, () -> updateUserPasswordCase.execute(user, paramsNula));
        assertEquals("newPassword", ex1.getDetails().get("campo"));
        NullOrEmptyException ex2 = assertThrows(NullOrEmptyException.class, () -> updateUserPasswordCase.execute(user, paramsVazia));
        assertEquals("newPassword", ex2.getDetails().get("campo"));
    }

    /**
     * Deve lançar exceção se a senha atual não corresponder à senha do usuário.
     */
    @Test
    void updateUserPasswordCase_deveLancarExcecao_quandoSenhaAtualIncorreta() {
        // Given
        UpdateUserPasswordParams params = new UpdateUserPasswordParams("NovaSenha@123", "SenhaErrada");
        when(user.getEncryptedPassword()).thenReturn(encryptedPassword);
        when(securityService.matchPasswords("SenhaErrada", encryptedPassword)).thenReturn(false);

        // When/Then
        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> updateUserPasswordCase.execute(user, params));
        assertEquals("Senha atual inválida.", ex.getMessage());
    }

}
