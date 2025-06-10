package br.dev.diisk.application.user.cases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.dev.diisk.application.user.dtos.UpdateUserParams;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.Email;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserPerfil;
import br.dev.diisk.domain.user.interfaces.IUserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes unitários para o caso de uso UpdateUserCase.
 * Cobre cenários felizes, limites e de exceção.
 */
@ExtendWith(MockitoExtension.class)
class UpdateUserCaseTest {
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UpdateUserCase updateUserCase;

    private User user;
    private UserPerfil perfil;

    @BeforeEach
    void setUp() {
        perfil = new UserPerfil("Perfil Teste", 1, null);
        user = new User("Nome Antigo", new Email("teste@email.com"), "senha123", perfil);
    }

    /**
     * Deve atualizar o nome do usuário quando o nome for válido.
     */
    @Test
    void updateUserCase_deveAtualizarNome_quandoNomeValido() {
        // Given
        UpdateUserParams params = new UpdateUserParams("Novo Nome");
        // When
        User result = updateUserCase.execute(user, params);
        // Then
        assertEquals("Novo Nome", result.getName());
        verify(userRepository).save(user);
    }

    /**
     * Não deve atualizar o usuário quando o nome for nulo.
     */
    @Test
    void updateUserCase_naoDeveAtualizar_quandoNomeNulo() {
        // Given
        UpdateUserParams params = new UpdateUserParams(null);
        // When
        User result = updateUserCase.execute(user, params);
        // Then
        assertEquals("Nome Antigo", result.getName());
        verify(userRepository, never()).save(user);
    }

    /**
     * Deve lançar exceção se o nome for vazio.
     */
    @Test
    void updateUserCase_deveLancarExcecao_quandoNomeVazio() {
        // Given
        UpdateUserParams params = new UpdateUserParams("");
        // When/Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> updateUserCase.execute(user, params));
        assertEquals("name", ex.getDetails().get("campo"));
    }

}
