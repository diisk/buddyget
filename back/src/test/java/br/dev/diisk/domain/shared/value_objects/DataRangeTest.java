package br.dev.diisk.domain.shared.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o objeto de valor DataRange.
 * Cada teste segue o padrão Given-When-Then e cobre cenários felizes, limites e
 * inválidos.
 */
@ExtendWith(MockitoExtension.class)
class DataRangeTest {

    // Teste: Deve criar DataRange válido
    @Test
    void dataRange_deveCriarValido_quandoDatasValidas() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 10, 0, 0);
        // When
        DataRange range = new DataRange(start, end);
        // Then
        assertEquals(start, range.getStartDate());
        assertEquals(end, range.getEndDate());
        // DataRange criado corretamente
    }

    // Teste: Deve lançar exceção se startDate for nulo
    @Test
    void dataRange_deveLancarExcecao_quandoStartDateNulo() {
        // Given
        LocalDateTime end = LocalDateTime.of(2024, 1, 10, 0, 0);
        // When/Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> new DataRange(null, end));
        assertTrue(ex.getDetails().get("campo").equals("startDate"));
        // Exceção lançada para startDate nulo
    }

    // Teste: Deve lançar exceção se endDate for antes de startDate
    @Test
    void dataRange_deveLancarExcecao_quandoEndDateAntesDeStartDate() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 0, 0);
        // When/Then
        BusinessException ex = assertThrows(BusinessException.class, () -> new DataRange(start, end));
        assertTrue(ex.getDetails().get("startDate").equals(start.toLocalDate().toString()));
        assertTrue(ex.getDetails().get("endDate").equals(end.toLocalDate().toString()));
        // Exceção lançada para endDate antes de startDate
    }

    // Teste: Deve considerar iguais DataRanges com mesmos valores
    @Test
    void dataRange_deveSerIgual_quandoValoresIguais() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 10, 0, 0);
        DataRange r1 = new DataRange(start, end);
        DataRange r2 = new DataRange(start, end);
        // When/Then
        assertEquals(r1, r2);
        // Objetos DataRange iguais
    }
}
