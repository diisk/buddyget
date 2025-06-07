package br.dev.diisk.application.cases.user;

import br.dev.diisk.application.dtos.user.UpdateUserParams;
import br.dev.diisk.application.services.ISecurityService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso UpdateUserCase.
 * Cada teste segue o padrão Given-When-Then e cobre cenários felizes, limites e inválidos.
 */
@ExtendWith(MockitoExtension.class)
class UpdateUserCaseTest {
    @Mock
    private IUserRepository userRepository;
    @Mock
    private ISecurityService securityService;
    @InjectMocks
    private UpdateUserCase updateUserCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("João");
        user.setEncryptedPassword("senhaAntiga");
    }

    // Teste: Atualiza apenas o nome quando diferente e válido
    @Test
    void updateUser_deveAtualizarNome_quandoNomeDiferenteEValido() {
        // Given
        UpdateUserParams params = new UpdateUserParams("Maria");
        // When
        User result = updateUserCase.execute(user, params);
        // Then
        assertEquals("Maria", result.getName());
        assertEquals("senhaAntiga", result.getEncryptedPassword());
        verify(userRepository).save((User) eq(user));
        // O nome deve ser atualizado e senha mantida
    }

    // Teste: Não atualiza nome se igual ao atual
    @Test
    void updateUser_naoDeveAtualizarNome_quandoNomeIgual() {
        // Given
        UpdateUserParams params = new UpdateUserParams("João");
        // When
        User result = updateUserCase.execute(user, params);
        // Then
        assertEquals("João", result.getName());
        verify(userRepository, never()).save(any(User.class));
        // O nome não deve ser alterado nem salvo
    }

    // Teste: Não atualiza nome se igual ao atual
    @Test
    void updateUser_naoDeveAtualizarNome_quandoNomeEmBranco() {
        // Given
        UpdateUserParams params = new UpdateUserParams("  ");
        // When
        User result = updateUserCase.execute(user, params);
        // Then
        assertEquals("João", result.getName());
        verify(userRepository, never()).save(any(User.class));
        // O nome não deve ser alterado nem salvo
    }

    // Teste: Não atualiza nome se igual ao atual
    @Test
    void updateUser_naoDeveAtualizarNome_quandoNomeVazio() {
        // Given
        UpdateUserParams params = new UpdateUserParams("");
        // When
        User result = updateUserCase.execute(user, params);
        // Then
        assertEquals("João", result.getName());
        verify(userRepository, never()).save(any(User.class));
        // O nome não deve ser alterado nem salvo
    }

    // Teste: Não salva se nada for alterado
    @Test
    void updateUser_naoDeveSalvar_quandoNadaAlterado() {
        // Given
        UpdateUserParams params = new UpdateUserParams(null);
        // When
        updateUserCase.execute(user, params);
        // Then
        verify(userRepository, never()).save(any(User.class));
        // Nenhuma alteração, não deve salvar
    }
}
