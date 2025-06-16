package br.dev.diisk.application.user.cases;

import br.dev.diisk.application.shared.services.ISecurityService;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.shared.value_objects.Password;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserPerfil;
import br.dev.diisk.domain.user.enums.UserPerfilEnum;
import br.dev.diisk.domain.user.interfaces.IUserPerfilRepository;
import br.dev.diisk.domain.user.interfaces.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddUserCaseTest {
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IUserPerfilRepository userPerfilRepository;
    @Mock
    private ISecurityService securityService;

    @InjectMocks
    private AddUserCase addUserCase;

    private final String name = "Test User";
    private final String email = "test@example.com";
    private final String password = "Senha@123";
    private final String encryptedPassword = "encryptedPassword";
    private final String perfilName = UserPerfilEnum.DEFAULT.name();

    // Teste feliz: usuário criado com sucesso
    @Test
    void addUser_deveCriarUsuario_quandoDadosValidos() {
        // Given
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());
        UserPerfil perfil = mock(UserPerfil.class);
        when(userPerfilRepository.findByName(perfilName)).thenReturn(perfil);
        when(securityService.encryptPassword(any(Password.class))).thenReturn(encryptedPassword);
        // When
        User result = addUserCase.execute(name, email, password);
        // Then
        assertNotNull(result, "O usuário criado não deve ser nulo");
        assertEquals(name, result.getName(), "O nome deve ser igual ao informado");
        assertEquals(email, result.getEmail(), "O email deve ser igual ao informado");
        assertEquals(encryptedPassword, result.getPassword(), "A senha deve estar criptografada");
        assertEquals(perfil, result.getPerfil(), "O perfil deve ser o padrão");
        verify(userRepository).save(any(User.class));
    }

    // Teste de conflito: email já cadastrado
    @Test
    void addUser_deveLancarDatabaseValueConflictException_quandoEmailJaCadastrado() {
        // Given
        User existingUser = mock(User.class);
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(existingUser));
        // When / Then
        DatabaseValueConflictException ex = assertThrows(DatabaseValueConflictException.class, () ->
                addUserCase.execute(name, email, password)
        );
        assertEquals(email, ex.getDetails().get("valor"), "O details deve conter o email em conflito");
    }

    // Teste de erro: perfil padrão não encontrado
    @Test
    void addUser_deveLancarDatabaseValueNotFoundException_quandoPerfilPadraoNaoEncontrado() {
        // Given
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());
        when(userPerfilRepository.findByName(perfilName)).thenReturn(null);
        // When / Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                addUserCase.execute(name, email, password)
        );
        assertEquals(perfilName, ex.getDetails().get("valor"), "O details deve conter o nome do perfil não encontrado");
    }

}
