package br.dev.diisk.application.finance;

import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso ListRecurringReferenceDates.
 * Segue o padrão Given-When-Then e cobre cenários felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class ListRecurringReferenceDatesTest {

    @Mock
    private UtilService utilService;

    @InjectMocks
    private ListRecurringReferenceDatesCase listRecurringReferenceDates;

    /**
     * Método auxiliar para criar uma ExpenseRecurring com período específico.
     */
    private ExpenseRecurring criarExpenseRecurringComPeriodo(LocalDateTime startDate, LocalDateTime endDate) {
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        Period period = new Period(startDate, endDate);
        
        ExpenseRecurring expenseRecurring = new ExpenseRecurring(
                "Despesa Recorrente Teste",
                period,
                category,
                new BigDecimal("100.00"),
                user);
        expenseRecurring.setId(1L);
        return expenseRecurring;
    }

    /**
     * Método auxiliar para criar uma ExpenseRecurring com período aberto (sem data de fim).
     */
    private ExpenseRecurring criarExpenseRecurringComPeriodoAberto(LocalDateTime startDate) {
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        Period period = new Period(startDate, null);
        
        ExpenseRecurring expenseRecurring = new ExpenseRecurring(
                "Despesa Recorrente Teste",
                period,
                category,
                new BigDecimal("100.00"),
                user);
        expenseRecurring.setId(1L);
        return expenseRecurring;
    }

    /**
     * Deve retornar lista de datas de referência quando período é de um mês completo.
     */
    @Test
    @DisplayName("listRecurringReferenceDates_deveRetornarListaComUmaData_quandoPeriodoEUmMes")
    void listRecurringReferenceDates_deveRetornarListaComUmaData_quandoPeriodoEUmMes() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 15, 10, 30);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 28, 15, 45);
        ExpenseRecurring recurring = criarExpenseRecurringComPeriodo(startDate, endDate);
        LocalDateTime firstDayReference = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime lastDayReference = LocalDateTime.of(2025, 1, 31, 23, 59, 59);
        
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayReference);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(lastDayReference);

        // When
        List<LocalDateTime> result = listRecurringReferenceDates.execute(recurring);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(firstDayReference, result.get(0));
        verify(utilService).getFirstDayMonthReference(startDate);
        verify(utilService).getLastDayMonthReference(endDate);
    }

    /**
     * Deve retornar lista de datas de referência quando período abrange múltiplos meses.
     */
    @Test
    @DisplayName("listRecurringReferenceDates_deveRetornarListaComMultiplasDatas_quandoPeriodoAbrageMultiplosMeses")
    void listRecurringReferenceDates_deveRetornarListaComMultiplasDatas_quandoPeriodoAbrageMultiplosMeses() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 15, 10, 30);
        LocalDateTime endDate = LocalDateTime.of(2025, 3, 28, 15, 45);
        ExpenseRecurring recurring = criarExpenseRecurringComPeriodo(startDate, endDate);
        LocalDateTime firstDayReference = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime lastDayReference = LocalDateTime.of(2025, 3, 31, 23, 59, 59);
        
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayReference);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(lastDayReference);

        // When
        List<LocalDateTime> result = listRecurringReferenceDates.execute(recurring);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0, 0), result.get(0));
        assertEquals(LocalDateTime.of(2025, 2, 1, 0, 0, 0), result.get(1));
        assertEquals(LocalDateTime.of(2025, 3, 1, 0, 0, 0), result.get(2));
        verify(utilService).getFirstDayMonthReference(startDate);
        verify(utilService).getLastDayMonthReference(endDate);
    }

    /**
     * Deve retornar lista com uma data quando período inicia e termina no mesmo mês.
     */
    @Test
    @DisplayName("listRecurringReferenceDates_deveRetornarListaComUmaData_quandoPeriodoIniciaETerminaNoMesmoMes")
    void listRecurringReferenceDates_deveRetornarListaComUmaData_quandoPeriodoIniciaETerminaNoMesmoMes() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 2, 5, 8, 15);
        LocalDateTime endDate = LocalDateTime.of(2025, 2, 25, 18, 30);
        ExpenseRecurring recurring = criarExpenseRecurringComPeriodo(startDate, endDate);
        LocalDateTime firstDayReference = LocalDateTime.of(2025, 2, 1, 0, 0, 0);
        LocalDateTime lastDayReference = LocalDateTime.of(2025, 2, 28, 23, 59, 59);
        
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayReference);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(lastDayReference);

        // When
        List<LocalDateTime> result = listRecurringReferenceDates.execute(recurring);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(firstDayReference, result.get(0));
        verify(utilService).getFirstDayMonthReference(startDate);
        verify(utilService).getLastDayMonthReference(endDate);
    }

    /**
     * Deve retornar lista com muitas datas quando período abrange muitos meses.
     */
    @Test
    @DisplayName("listRecurringReferenceDates_deveRetornarListaComMuitasDatas_quandoPeriodoAbrangeMuitosMeses")
    void listRecurringReferenceDates_deveRetornarListaComMuitasDatas_quandoPeriodoAbrangeMuitosMeses() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 15, 10, 30);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 28, 15, 45);
        ExpenseRecurring recurring = criarExpenseRecurringComPeriodo(startDate, endDate);
        LocalDateTime firstDayReference = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime lastDayReference = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayReference);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(lastDayReference);

        // When
        List<LocalDateTime> result = listRecurringReferenceDates.execute(recurring);

        // Then
        assertNotNull(result);
        assertEquals(24, result.size()); // 24 meses de 2024-01 até 2025-12
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0, 0), result.get(0));
        assertEquals(LocalDateTime.of(2024, 2, 1, 0, 0, 0), result.get(1));
        assertEquals(LocalDateTime.of(2025, 11, 1, 0, 0, 0), result.get(22));
        assertEquals(LocalDateTime.of(2025, 12, 1, 0, 0, 0), result.get(23));
        verify(utilService).getFirstDayMonthReference(startDate);
        verify(utilService).getLastDayMonthReference(endDate);
    }

    /**
     * Deve retornar lista com datas corretas quando período é exatamente de dois meses consecutivos.
     */
    @Test
    @DisplayName("listRecurringReferenceDates_deveRetornarListaComDuasDatas_quandoPeriodoEDoisMesesConsecutivos")
    void listRecurringReferenceDates_deveRetornarListaComDuasDatas_quandoPeriodoEDoisMesesConsecutivos() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 10, 14, 20);
        LocalDateTime endDate = LocalDateTime.of(2025, 7, 20, 16, 40);
        ExpenseRecurring recurring = criarExpenseRecurringComPeriodo(startDate, endDate);
        LocalDateTime firstDayReference = LocalDateTime.of(2025, 6, 1, 0, 0, 0);
        LocalDateTime lastDayReference = LocalDateTime.of(2025, 7, 31, 23, 59, 59);
        
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayReference);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(lastDayReference);

        // When
        List<LocalDateTime> result = listRecurringReferenceDates.execute(recurring);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(LocalDateTime.of(2025, 6, 1, 0, 0, 0), result.get(0));
        assertEquals(LocalDateTime.of(2025, 7, 1, 0, 0, 0), result.get(1));
        verify(utilService).getFirstDayMonthReference(startDate);
        verify(utilService).getLastDayMonthReference(endDate);
    }

    /**
     * Deve usar LocalDateTime.now() como data de fim quando getEndDate() retorna null.
     */
    @Test
    @DisplayName("listRecurringReferenceDates_deveUsarDataAtualComoFim_quandoGetEndDateRetornaNulo")
    void listRecurringReferenceDates_deveUsarDataAtualComoFim_quandoGetEndDateRetornaNulo() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 10, 14, 20);
        ExpenseRecurring recurring = criarExpenseRecurringComPeriodoAberto(startDate);
        LocalDateTime firstDayReference = LocalDateTime.of(2025, 6, 1, 0, 0, 0);
        LocalDateTime lastDayReference = LocalDateTime.of(2025, 8, 31, 23, 59, 59);
        
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayReference);
        when(utilService.getLastDayMonthReference(any(LocalDateTime.class))).thenReturn(lastDayReference);

        // When
        List<LocalDateTime> result = listRecurringReferenceDates.execute(recurring);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size()); // Junho, Julho, Agosto
        assertEquals(LocalDateTime.of(2025, 6, 1, 0, 0, 0), result.get(0));
        assertEquals(LocalDateTime.of(2025, 7, 1, 0, 0, 0), result.get(1));
        assertEquals(LocalDateTime.of(2025, 8, 1, 0, 0, 0), result.get(2));
        verify(utilService).getFirstDayMonthReference(startDate);
        verify(utilService).getLastDayMonthReference(any(LocalDateTime.class));
    }
}
