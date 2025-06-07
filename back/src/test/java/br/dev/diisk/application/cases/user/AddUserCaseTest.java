package br.dev.diisk.application.cases.user;

import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.entities.user.UserPerfil;
import br.dev.diisk.domain.enums.user.UserPerfilEnum;
import br.dev.diisk.domain.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.repositories.user.IUserPerfilRepository;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddUserCaseTest {

    @Mock
    IUserRepository userRepository;

    @Mock
    IUserPerfilRepository userPerfilRepository;

    @InjectMocks
    AddUserCase addUserCase;

    // Cenário: Deve criar usuário com sucesso quando e-mail não existe e perfil
    // padrão está presente
    @Test
    void execute_deveCriarUsuario_quandoEmailNaoExisteEPerfilPadraoPresente() {
        // Given: Usuário não existe e perfil padrão existe
        String name = "João";
        String email = "joao@email.com";
        String password = "senha123";
        UserPerfil perfil = new UserPerfil(); // pode ser mockado se necessário

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userPerfilRepository.findByName(UserPerfilEnum.DEFAULT.name())).thenReturn(perfil);
        // When: Executa o caso de uso
        User result = addUserCase.execute(name, email, password);

        // Then: Usuário criado e salvo
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(perfil, result.getPerfil());
        verify(userRepository).save(any(User.class));
    }

    // Cenário: Deve lançar exceção de conflito quando e-mail já existe
    @Test
    void execute_deveLancarExcecaoDeConflito_quandoEmailJaExiste() {
        // Given: Usuário já existe com o e-mail informado
        String email = "joao@email.com";
        User existingUser = mock(User.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When/Then: Espera exceção de conflito
        assertThrows(DatabaseValueConflictException.class, () -> addUserCase.execute("João", email, "senha123"));
        verify(userRepository, never()).save(any(User.class));
    }

    // Cenário: Deve lançar exceção de não encontrado quando perfil padrão não
    // existe
    @Test
    void execute_deveLancarExcecaoDeNaoEncontrado_quandoPerfilPadraoNaoExiste() {
        // Given: Usuário não existe, mas perfil padrão não encontrado
        String email = "joao@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userPerfilRepository.findByName(UserPerfilEnum.DEFAULT.name())).thenReturn(null);

        // When/Then: Espera exceção de não encontrado
        assertThrows(DatabaseValueNotFoundException.class, () -> addUserCase.execute("João", email, "senha123"));
        verify(userRepository, never()).save(any(User.class));
    }
}
