package br.dev.diisk.domain.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.domain.exceptions.BusinessException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o objeto de valor Password.
 * Cada teste segue o padrão Given-When-Then e cobre cenários felizes, limites e inválidos.
 */
@ExtendWith(MockitoExtension.class)
class PasswordTest {

    // Teste: Deve criar senha válida
    @Test
    void password_deveCriarSenhaValida_quandoSenhaAtendeRequisitos() {
        // Given
        String senhaValida = "Abcdef1!";
        // When
        Password password = new Password(senhaValida);
        // Then
        assertEquals(senhaValida, password.getValue());
        // Senha criada corretamente
    }

    // Teste: Deve lançar exceção para senha inválida
    @Test
    void password_deveLancarExcecao_quandoSenhaInvalida() {
        // Given
        String senhaInvalida = "abc";
        // When/Then
        BusinessException ex = assertThrows(BusinessException.class, () -> new Password(senhaInvalida));
        assertTrue(ex.getDetails().get("senha").equals(senhaInvalida));
        // Exceção lançada para senha inválida
    }

    // Teste: Deve considerar iguais senhas com mesmo valor
    @Test
    void password_deveSerIgual_quandoValoresIguais() {
        // Given
        Password p1 = new Password("Abcdef1!");
        Password p2 = new Password("Abcdef1!");
        // When/Then
        assertEquals(p1, p2);
        // Objetos Password iguais
    }

    // Teste: Deve lançar exceção para senha curta demais
    @Test
    void password_deveLancarExcecao_quandoSenhaMuitoCurta() {
        String senhaCurta = "Ab1!";
        assertThrows(BusinessException.class, () -> new Password(senhaCurta));
    }

    // Teste: Deve lançar exceção para senha sem letra maiúscula
    @Test
    void password_deveLancarExcecao_quandoSemLetraMaiuscula() {
        String senhaSemMaiuscula = "abcdef1!";
        assertThrows(BusinessException.class, () -> new Password(senhaSemMaiuscula));
    }

    // Teste: Deve lançar exceção para senha sem letra minúscula
    @Test
    void password_deveLancarExcecao_quandoSemLetraMinuscula() {
        String senhaSemMinuscula = "ABCDEF1!";
        assertThrows(BusinessException.class, () -> new Password(senhaSemMinuscula));
    }

    // Teste: Deve lançar exceção para senha sem número
    @Test
    void password_deveLancarExcecao_quandoSemNumero() {
        String senhaSemNumero = "Abcdefg!";
        assertThrows(BusinessException.class, () -> new Password(senhaSemNumero));
    }

    // Teste: Deve lançar exceção para senha sem caractere especial
    @Test
    void password_deveLancarExcecao_quandoSemCaractereEspecial() {
        String senhaSemEspecial = "Abcdefg1";
        assertThrows(BusinessException.class, () -> new Password(senhaSemEspecial));
    }
}
