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

import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.expense.ListFixedExpenseFilter;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;

public class ListFixedExpenseCaseTest {

    @Mock
    private IFixedExpenseRepository fixedExpenseRepository;

    @InjectMocks
    private ListFixedExpenseCase listFixedExpenseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void execute_quandoFiltroValido_DeveRetornarPaginaDeDespesasFixas() {
        // Given
        User user = new User();
        user.setId(1L);
        ListFixedExpenseFilter filter = new ListFixedExpenseFilter();
        Pageable pageable = PageRequest.of(0, 10);
        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setId(1L);
        Page<FixedExpense> expectedPage = new PageImpl<>(Collections.singletonList(fixedExpense));

        when(fixedExpenseRepository.findBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When
        Page<FixedExpense> result = listFixedExpenseCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        assertEquals(1, result.getTotalElements());
        assertEquals(fixedExpense, result.getContent().get(0));
    }

    @Test
    public void execute_quandoNenhumaDespesaEncontrada_DeveRetornarPaginaVazia() {
        // Given
        User user = new User();
        user.setId(1L);
        ListFixedExpenseFilter filter = new ListFixedExpenseFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<FixedExpense> expectedPage = new PageImpl<>(Collections.emptyList());

        when(fixedExpenseRepository.findBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When
        Page<FixedExpense> result = listFixedExpenseCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        assertEquals(0, result.getTotalElements());
    }
}
