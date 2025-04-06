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
import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;

public class GetFixedExpenseCaseTest {

    @Mock
    private IFixedExpenseRepository fixedExpenseRepository;

    @InjectMocks
    private GetFixedExpenseCase getFixedExpenseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getFixedExpenseCase_quandoDespesaFixaExiste_DeveRetornarDespesa() {
        // Given
        User user = new User();
        user.setId(1L);
        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setId(1L);
        fixedExpense.setUser(user);

        when(fixedExpenseRepository.findById(1L)).thenReturn(Optional.of(fixedExpense));

        // When
        FixedExpense result = getFixedExpenseCase.execute(user, 1L);

        // Then
        assertEquals(fixedExpense, result);
    }

    @Test
    public void getFixedExpenseCase_quandoDespesaFixaNaoExiste_DeveLancarDbValueNotFoundException() {
        // Given
        User user = new User();
        user.setId(1L);

        when(fixedExpenseRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            getFixedExpenseCase.execute(user, 1L);
        });
    }

    @Test
    public void getFixedExpenseCase_quandoDespesaFixaNaoPertenceAoUsuario_DeveLancarDbValueNotFoundException() {
        // Given
        User user = new User();
        user.setId(1L);
        User otherUser = new User();
        otherUser.setId(2L);
        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setId(1L);
        fixedExpense.setUser(otherUser);

        when(fixedExpenseRepository.findById(1L)).thenReturn(Optional.of(fixedExpense));

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            getFixedExpenseCase.execute(user, 1L);
        });
    }
}
