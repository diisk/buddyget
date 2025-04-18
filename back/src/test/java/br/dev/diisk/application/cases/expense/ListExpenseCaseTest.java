package br.dev.diisk.application.cases.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.expense.ListExpenseFilter;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;

public class ListExpenseCaseTest {

    @Mock
    private IExpenseRepository expenseRepository;

    @InjectMocks
    private ListExpenseCase listExpenseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listExpenseCase_quandoFiltroValido_DeveRetornarPaginaDeDespesas() {
        // Given
        User user = new User();
        user.setId(1L);
        ListExpenseFilter filter = new ListExpenseFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Expense expense = new Expense();
        expense.setId(1L);
        Page<Expense> expectedPage = new PageImpl<>(Collections.singletonList(expense));

        when(expenseRepository.findBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When
        Page<Expense> result = listExpenseCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        assertEquals(1, result.getTotalElements());
        assertEquals(expense, result.getContent().get(0));
    }

    @Test
    public void listExpenseCase_quandoNenhumaDespesaEncontrada_DeveRetornarPaginaVazia() {
        // Given
        User user = new User();
        user.setId(1L);
        ListExpenseFilter filter = new ListExpenseFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Expense> expectedPage = new PageImpl<>(Collections.emptyList());

        when(expenseRepository.findBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When
        Page<Expense> result = listExpenseCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        assertEquals(0, result.getTotalElements());
    }
}
