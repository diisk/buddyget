package br.dev.diisk.application.finance.expense_recurring.cases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_recurring.ListExpenseRecurringsFilter;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

@ExtendWith(MockitoExtension.class)
class ListExpenseRecurringsCaseTest {

    @Mock
    private IExpenseRecurringRepository expenseRecurringRepository;

    @InjectMocks
    private ListExpenseRecurringsCase listExpenseRecurringsCase;

    @Test
    void listExpenseRecurrings_deveExecutarCorretamente_quandoChamado() {
        // Given - prepara os dados básicos para teste simples de execução
        User user = UserFixture.umUsuarioComId(1L);
        ListExpenseRecurringsFilter filter = new ListExpenseRecurringsFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ExpenseRecurring> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(expenseRecurringRepository.findAllBy(any(Long.class), any(), any(Pageable.class)))
                .thenReturn(emptyPage);

        // When - executa o método a ser testado
        Page<ExpenseRecurring> result = listExpenseRecurringsCase.execute(user, filter, pageable);

        // Then - verifica que o método foi executado corretamente
        assertNotNull(result);
        verify(expenseRecurringRepository).findAllBy(user.getId(), filter, pageable);
    }

    @Test
    void listExpenseRecurrings_deveRetornarPaginaVazia_quandoNaoExistiremExpenseRecurrings() {
        // Given - prepara os dados e mocks para resultado vazio
        User user = UserFixture.umUsuarioComId(2L);
        ListExpenseRecurringsFilter filter = new ListExpenseRecurringsFilter();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ExpenseRecurring> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(expenseRecurringRepository.findAllBy(eq(2L), eq(filter), eq(pageable)))
                .thenReturn(emptyPage);

        // When - executa o método a ser testado
        Page<ExpenseRecurring> result = listExpenseRecurringsCase.execute(user, filter, pageable);

        // Then - verifica os resultados da página vazia
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());

        verify(expenseRecurringRepository).findAllBy(2L, filter, pageable);
    }

    @Test
    void listExpenseRecurrings_devePassarParametrosCorretamente_quandoFiltroForNulo() {
        // Given - prepara os dados com filtro nulo
        User user = UserFixture.umUsuarioComId(3L);
        ListExpenseRecurringsFilter filter = null;
        Pageable pageable = PageRequest.of(1, 5);

        Page<ExpenseRecurring> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(expenseRecurringRepository.findAllBy(eq(3L), eq(filter), eq(pageable)))
                .thenReturn(emptyPage);

        // When - executa o método a ser testado
        Page<ExpenseRecurring> result = listExpenseRecurringsCase.execute(user, filter, pageable);

        // Then - verifica que os parâmetros foram passados corretamente
        assertNotNull(result);
        assertEquals(1, result.getNumber());
        assertEquals(5, result.getSize());

        verify(expenseRecurringRepository).findAllBy(3L, filter, pageable);
    }

    @Test
    void listExpenseRecurrings_devePassarFiltroComSearchString_quandoFiltroInformado() {
        // Given - prepara os dados com filtro contendo searchString
        User user = UserFixture.umUsuarioComId(4L);
        ListExpenseRecurringsFilter filter = new ListExpenseRecurringsFilter();
        filter.setSearchString("despesa teste");
        Pageable pageable = PageRequest.of(0, 20);

        Page<ExpenseRecurring> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(expenseRecurringRepository.findAllBy(eq(4L), eq(filter), eq(pageable)))
                .thenReturn(emptyPage);

        // When - executa o método a ser testado
        Page<ExpenseRecurring> result = listExpenseRecurringsCase.execute(user, filter, pageable);

        // Then - verifica que os parâmetros foram passados corretamente
        assertNotNull(result);
        assertEquals(0, result.getNumber());
        assertEquals(20, result.getSize());

        verify(expenseRecurringRepository).findAllBy(4L, filter, pageable);
    }

    @Test
    void listExpenseRecurrings_devePassarParametrosDePaginacao_corretamente() {
        // Given - prepara os dados com configurações específicas de paginação
        User user = UserFixture.umUsuarioComId(5L);
        ListExpenseRecurringsFilter filter = new ListExpenseRecurringsFilter();
        filter.setSearchString("despesa mensal");
        Pageable pageable = PageRequest.of(2, 15);

        Page<ExpenseRecurring> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(expenseRecurringRepository.findAllBy(eq(5L), eq(filter), eq(pageable)))
                .thenReturn(emptyPage);

        // When - executa o método a ser testado
        Page<ExpenseRecurring> result = listExpenseRecurringsCase.execute(user, filter, pageable);

        // Then - verifica que os parâmetros de paginação foram passados corretamente
        assertNotNull(result);
        assertEquals(2, result.getNumber());
        assertEquals(15, result.getSize());

        verify(expenseRecurringRepository).findAllBy(5L, filter, pageable);
    }
}
