package br.dev.diisk.domain.shared.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.domain.shared.exceptions.BusinessException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o objeto de valor HexadecimalColor.
 * Cada teste segue o padrão Given-When-Then e cobre cenários felizes, limites e inválidos.
 */
@ExtendWith(MockitoExtension.class)
class HexadecimalColorTest {

    // Teste: Deve criar cor hexadecimal válida
    @Test
    void hexadecimalColor_deveCriarCorValida_quandoHexValido() {
        // Given
        String hex = "#FFAABB";
        // When
        HexadecimalColor color = new HexadecimalColor(hex);
        // Then
        assertEquals(hex, color.getValue());
        // Cor criada corretamente
    }

    // Teste: Deve lançar exceção para hexadecimal inválido
    @Test
    void hexadecimalColor_deveLancarExcecao_quandoHexInvalido() {
        // Given
        String hexInvalido = "123456";
        // When/Then
        BusinessException ex = assertThrows(BusinessException.class, () -> new HexadecimalColor(hexInvalido));
        assertTrue(ex.getDetails().get("color").equals(hexInvalido));
        // Exceção lançada para hexadecimal inválido
    }

    // Teste: Deve considerar iguais cores com mesmo valor
    @Test
    void hexadecimalColor_deveSerIgual_quandoValoresIguais() {
        // Given
        HexadecimalColor c1 = new HexadecimalColor("#FFF");
        HexadecimalColor c2 = new HexadecimalColor("#FFF");
        // When/Then
        assertEquals(c1, c2);
        // Objetos HexadecimalColor iguais
    }
}
