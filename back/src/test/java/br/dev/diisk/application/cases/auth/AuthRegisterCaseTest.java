package br.dev.diisk.application.cases.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.dtos.auth.RegisterRequest;
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
        RegisterRequest request = new RegisterRequest("john@example.com", "John Doe", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // When & Then
        assertThrows(ValueAlreadyInDatabaseException.class, () -> {
            authRegisterCase.execute(request);
        });
    }

    @Test
    public void authRegisterCase_quandoPerfilPadraoNaoEncontrado_DeveLancarExcecao() {
        // Given
        RegisterRequest request = new RegisterRequest("john@example.com", "John Doe", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userPerfilRepository.findByName(anyString())).thenReturn(null);

        // When & Then
        assertThrows(DbValueNotFoundException.class, () -> {
            authRegisterCase.execute(request);
        });
    }

    @Test
    public void authRegisterCase_quandoDadosValidos_DeveRegistrarUsuario() {
        // Given
        RegisterRequest request = new RegisterRequest("john@example.com", "John Doe", "password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userPerfilRepository.findByName(anyString())).thenReturn(new UserPerfil());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // When
        User result = authRegisterCase.execute(request);
        
        // Then
        assertNotNull(result.getId());
        assertEquals(request.getEmail(), result.getEmail());
    }
}
