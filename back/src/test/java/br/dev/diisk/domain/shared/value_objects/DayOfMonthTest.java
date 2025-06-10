package br.dev.diisk.domain.shared.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.domain.shared.exceptions.BusinessException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o objeto de valor DayOfMonth.
 * Cada teste segue o padrão Given-When-Then e cobre cenários felizes, limites e inválidos.
 */
@ExtendWith(MockitoExtension.class)
class DayOfMonthTest {

    // Teste: Deve criar DayOfMonth válido
    @Test
    void dayOfMonth_deveCriarValido_quandoValorValido() {
        // Given
        Integer valor = 15;
        // When
        DayOfMonth day = new DayOfMonth(valor);
        // Then
        assertEquals(valor, day.getValue());
        // DayOfMonth criado corretamente
    }

    // Teste: Deve lançar exceção se valor estivar fora do intervalo
    @Test
    void dayOfMonth_deveLancarExcecao_quandoValorForaDoIntervalo() {
        // Given
        Integer valor = 0;
        Integer valor2 = 29;
        // When/Then
        BusinessException ex = assertThrows(BusinessException.class, () -> new DayOfMonth(valor));
        assertTrue(ex.getDetails().containsValue(valor.toString()));
        // Exceção lançada para valor menor que mínimo

        ex = assertThrows(BusinessException.class, () -> new DayOfMonth(valor2));
        assertTrue(ex.getDetails().get("valor").equals(valor2.toString()));
        // Exceção lançada para valor menor que mínimo
    }


    // Teste: Deve considerar iguais DayOfMonth com mesmos valores
    @Test
    void dayOfMonth_deveSerIgual_quandoValoresIguais() {
        // Given
        DayOfMonth d1 = new DayOfMonth(10);
        DayOfMonth d2 = new DayOfMonth(10);
        // When/Then
        assertEquals(d1, d2);
        // Objetos DayOfMonth iguais
    }
}
