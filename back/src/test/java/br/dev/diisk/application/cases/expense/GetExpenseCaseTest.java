package br.dev.diisk.application.cases.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;

public class GetExpenseCaseTest {

    @Mock
    private IExpenseRepository expenseRepository;

    @InjectMocks
    private GetExpenseCase getExpenseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getExpenseCase_quandoDespesaExiste_DeveRetornarDespesa() {
        // Given
        User user = new User();
        user.setId(1L);
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setUser(user);

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        // When
        Expense result = getExpenseCase.execute(user, 1L);

        // Then
        assertEquals(expense, result);
    }

    @Test
    public void getExpenseCase_quandoDespesaNaoExiste_DeveLancarDbValueNotFoundException() {
        // Given
        User user = new User();
        user.setId(1L);

        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            getExpenseCase.execute(user, 1L);
        });
    }

    @Test
    public void getExpenseCase_quandoDespesaNaoPertenceAoUsuario_DeveLancarDbValueNotFoundException() {
        // Given
        User user = new User();
        user.setId(1L);
        User otherUser = new User();
        otherUser.setId(2L);
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setUser(otherUser);

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            getExpenseCase.execute(user, 1L);
        });
    }
}
