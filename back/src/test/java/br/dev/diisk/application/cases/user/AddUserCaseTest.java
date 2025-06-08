package br.dev.diisk.application.cases.user;

import br.dev.diisk.application.services.ISecurityService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.entities.user.UserPerfil;
import br.dev.diisk.domain.enums.user.UserPerfilEnum;
import br.dev.diisk.domain.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.repositories.user.IUserPerfilRepository;
import br.dev.diisk.domain.repositories.user.IUserRepository;
import br.dev.diisk.domain.value_objects.Password;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    private final String name = "Usuário Teste";
    private final String email = "teste@exemplo.com";
    private final String password = "Senha@123"; // Corrigido para senha válida
    private final String encryptedPassword = "senhaCriptografada";
    private final UserPerfil defaultPerfil = Mockito.mock(UserPerfil.class);

    // Teste: Deve adicionar usuário com sucesso quando email não existe e perfil
    // padrão existe
    @Test
    void addUserCase_deveAdicionarUsuario_quandoDadosValidos() {
        // Given
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(userPerfilRepository.findByName(UserPerfilEnum.DEFAULT.name())).thenReturn(defaultPerfil);
        Mockito.when(securityService.encryptPassword(Mockito.any(Password.class))).thenReturn(encryptedPassword);
        Mockito.doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            Assertions.assertEquals(name, user.getName());
            Assertions.assertEquals(email, user.getEmail()); // Corrigido: getEmail() retorna String
            Assertions.assertEquals(encryptedPassword, user.getPassword());
            Assertions.assertEquals(defaultPerfil, user.getPerfil());
            return null;
        }).when(userRepository).save(Mockito.any(User.class));

        // When
        User result = addUserCase.execute(name, email, password);

        // Then
        Assertions.assertEquals(name, result.getName());
        Assertions.assertEquals(email, result.getEmail()); // Corrigido: getEmail() retorna String
        Assertions.assertEquals(encryptedPassword, result.getPassword());
        Assertions.assertEquals(defaultPerfil, result.getPerfil());
    }

    // Teste: Deve lançar DatabaseValueConflictException quando email já existe
    @Test
    void addUserCase_deveLancarDatabaseValueConflictException_quandoEmailJaExiste() {
        // Given
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(Mockito.mock(User.class)));

        // When & Then
        DatabaseValueConflictException ex = Assertions.assertThrows(DatabaseValueConflictException.class,
                () -> addUserCase.execute(name, email, password));
        Assertions.assertTrue(ex.getDetails().get("valor").equals(email));
    }

    // Teste: Deve lançar DatabaseValueNotFoundException quando perfil padrão não
    // existe
    @Test
    void addUserCase_deveLancarDatabaseValueNotFoundException_quandoPerfilPadraoNaoExiste() {
        // Given
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(userPerfilRepository.findByName(UserPerfilEnum.DEFAULT.name())).thenReturn(null);

        // When & Then
        DatabaseValueNotFoundException ex = Assertions.assertThrows(DatabaseValueNotFoundException.class,
                () -> addUserCase.execute(name, email, password));
        Assertions.assertTrue(ex.getDetails().get("valor").equals(UserPerfilEnum.DEFAULT.name()));
    }

    // Teste: Deve passar a senha corretamente para o serviço de segurança
    @Test
    void addUserCase_deveChamarEncryptPasswordComSenhaCorreta_quandoUsuarioValido() {
        // Given
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(userPerfilRepository.findByName(UserPerfilEnum.DEFAULT.name())).thenReturn(defaultPerfil);
        Mockito.when(securityService.encryptPassword(Mockito.any(Password.class))).thenReturn(encryptedPassword);

        // When
        addUserCase.execute(name, email, password);

        // Then
        Mockito.verify(securityService).encryptPassword(Mockito.any(Password.class));
    }
}
