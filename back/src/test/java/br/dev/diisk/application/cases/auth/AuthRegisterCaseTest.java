package br.dev.diisk.application.cases.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.entities.user.UserPerfil;
import br.dev.diisk.domain.repositories.user.IUserPerfilRepository;
import br.dev.diisk.domain.repositories.user.IUserRepository;

public class AuthRegisterCaseTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserPerfilRepository userPerfilRepository;

    @InjectMocks
    private AuthRegisterCase authRegisterCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authRegisterCase_quandoUsuarioExiste_DeveLancarExcecao() {
        // Given
        String email = "john@example.com";
        String name = "John Doe";
        String password = "password";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // When & Then
        assertThrows(ValueAlreadyInDatabaseException.class, () -> {
            authRegisterCase.execute(name, email, password);
        });
    }

    @Test
    public void authRegisterCase_quandoPerfilPadraoNaoEncontrado_DeveLancarExcecao() {
        // Given
        String email = "john@example.com";
        String name = "John Doe";
        String password = "password";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userPerfilRepository.findByName("DEFAULT")).thenReturn(null);

        // When & Then
        assertThrows(DbValueNotFoundException.class, () -> {
            authRegisterCase.execute(email, name, password);
        });
    }

    @Test
    public void authRegisterCase_quandoDadosValidos_DeveRegistrarUsuario() {
        // Given
        String email = "john@example.com";
        String name = "John Doe";
        String password = "password";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userPerfilRepository.findByName("DEFAULT")).thenReturn(new UserPerfil());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // When
        User result = authRegisterCase.execute(name, email, password);
        
        // Then
        assertNotNull(result.getId());
        assertEquals(email, result.getEmail());
    }
}
