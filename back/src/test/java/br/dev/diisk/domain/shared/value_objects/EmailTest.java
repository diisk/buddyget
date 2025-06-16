package br.dev.diisk.domain.shared.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o objeto de valor Email.
 * Cada teste segue o padrão Given-When-Then e cobre cenários felizes, limites e inválidos.
 */
@ExtendWith(MockitoExtension.class)
class EmailTest {

    // Teste: Deve criar Email válido
    @Test
    void email_deveCriarValido_quandoEmailValido() {
        // Given
        String emailValido = "teste@exemplo.com";
        // When
        Email email = new Email(emailValido);
        // Then
        assertEquals(emailValido, email.getValue());
        // Email criado corretamente
    }

    // Teste: Deve lançar exceção para email inválido
    @Test
    void email_deveLancarExcecao_quandoEmailInvalido() {
        // Given
        String emailInvalido = "invalido";
        // When/Then
        BusinessException ex = assertThrows(BusinessException.class, () -> new Email(emailInvalido));
        assertTrue(ex.getDetails().get("email").equals(emailInvalido));
        // Exceção lançada para email inválido
    }

    // Teste: Deve lançar exceção para email nulo
    @Test
    void email_deveLancarExcecao_quandoEmailNulo() {
        // When/Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> new Email(null));
        assertTrue(ex.getDetails().get("campo").equals("email"));
        // Exceção lançada para email inválido
    }

    // Teste: Deve considerar iguais emails com mesmo valor
    @Test
    void email_deveSerIgual_quandoValoresIguais() {
        // Given
        Email e1 = new Email("abc@x.com");
        Email e2 = new Email("abc@x.com");
        // When/Then
        assertEquals(e1, e2);
        // Objetos Email iguais
    }
}
