package br.dev.diisk.application.cases.user;

import br.dev.diisk.application.dtos.user.UpdateUserPasswordParams;
import br.dev.diisk.application.services.ISecurityService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.exceptions.UnauthorizedException;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import br.dev.diisk.domain.value_objects.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserPasswordCaseTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private ISecurityService securityService;
    @Mock
    private User user;
    @InjectMocks
    private UpdateUserPasswordCase updateUserPasswordCase;

    private final String currentPassword = "Senha@123";
    private final String newPassword = "NovaSenha@123";
    private final String encryptedCurrentPassword = "encryptedCurrent";
    private final String encryptedNewPassword = "encryptedNew";

    @BeforeEach
    void setUp() {
        updateUserPasswordCase = new UpdateUserPasswordCase(userRepository, securityService);
    }

    // Teste: deve atualizar a senha corretamente quando os dados são válidos
    @Test
    @DisplayName("updateUserPassword_deveAtualizarCorretamente_quandoDadosValidos")
    void updateUserPassword_deveAtualizarCorretamente_quandoDadosValidos() {
        // Given
        UpdateUserPasswordParams params = new UpdateUserPasswordParams(newPassword, currentPassword);
        when(securityService.matchPasswords(currentPassword, encryptedCurrentPassword)).thenReturn(true);
        when(securityService.encryptPassword(new Password(newPassword))).thenReturn(encryptedNewPassword);
        when(user.getEncryptedPassword()).thenReturn(encryptedCurrentPassword);

        // When
        updateUserPasswordCase.execute(user, params);

        // Then
        verify(user).setEncryptedPassword(encryptedNewPassword);
        verify(userRepository).save(user);
    }

    // Teste: deve lançar exceção se a senha atual for nula
    @Test
    @DisplayName("updateUserPassword_deveLancarExcecao_quandoSenhaAtualNula")
    void updateUserPassword_deveLancarExcecao_quandoSenhaAtualNula() {
        // Given
        UpdateUserPasswordParams params = new UpdateUserPasswordParams(newPassword, null);

        // When & Then
        assertThrows(NullOrEmptyException.class, () ->
                updateUserPasswordCase.execute(user, params));
    }

    // Teste: deve lançar exceção se a nova senha for nula
    @Test
    @DisplayName("updateUserPassword_deveLancarExcecao_quandoNovaSenhaNula")
    void updateUserPassword_deveLancarExcecao_quandoNovaSenhaNula() {
        // Given
        UpdateUserPasswordParams params = new UpdateUserPasswordParams(null, currentPassword);

        assertThrows(NullOrEmptyException.class, () ->
                updateUserPasswordCase.execute(user, params));
    }

    // Teste: deve lançar exceção se a senha atual estiver incorreta
    @Test
    @DisplayName("updateUserPassword_deveLancarExcecao_quandoSenhaAtualIncorreta")
    void updateUserPassword_deveLancarExcecao_quandoSenhaAtualIncorreta() {
        // Given
        UpdateUserPasswordParams params = new UpdateUserPasswordParams(newPassword, currentPassword);
        when(securityService.matchPasswords(currentPassword, encryptedCurrentPassword)).thenReturn(false);
        when(user.getEncryptedPassword()).thenReturn(encryptedCurrentPassword);

        // When & Then
        assertThrows(UnauthorizedException.class, () ->
                updateUserPasswordCase.execute(user, params));
    }

    // Teste: deve lançar exceção se a senha atual for vazia
    @Test
    @DisplayName("updateUserPassword_deveLancarExcecao_quandoSenhaAtualVazia")
    void updateUserPassword_deveLancarExcecao_quandoSenhaAtualVazia() {
        // Given
        UpdateUserPasswordParams params = new UpdateUserPasswordParams(newPassword, "");

        // When & Then
        assertThrows(NullOrEmptyException.class, () ->
                updateUserPasswordCase.execute(user, params));

    }

    // Teste: deve lançar exceção se a nova senha for vazia
    @Test
    @DisplayName("updateUserPassword_deveLancarExcecao_quandoNovaSenhaVazia")
    void updateUserPassword_deveLancarExcecao_quandoNovaSenhaVazia() {
        // Given
        UpdateUserPasswordParams params = new UpdateUserPasswordParams("", currentPassword);

        assertThrows(NullOrEmptyException.class, () ->
                updateUserPasswordCase.execute(user, params));
    }
}
