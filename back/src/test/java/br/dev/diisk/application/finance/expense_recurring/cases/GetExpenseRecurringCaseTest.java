package br.dev.diisk.application.finance.expense_recurring.cases;

import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso GetExpenseRecurringCase.
 */
@ExtendWith(MockitoExtension.class)
class GetExpenseRecurringCaseTest {

    @Mock
    private IExpenseRecurringRepository expenseRecurringRepository;

    @InjectMocks
    private GetExpenseRecurringCase getExpenseRecurringCase;

    // Teste para o caminho feliz: deve retornar a despesa recorrente quando encontrada e pertence ao usuário
    @Test
    void getExpenseRecurring_deveRetornarDespesaRecorrente_quandoEncontradaEUsuarioCorreto() {
        // Given
        Long expenseRecurringId = 1L;
        Long userId = 10L;
        User user = mock(User.class);
        ExpenseRecurring expenseRecurring = mock(ExpenseRecurring.class);
        when(user.getId()).thenReturn(userId);
        when(expenseRecurring.getUserId()).thenReturn(userId);
        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));

        // When
        ExpenseRecurring result = getExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then
        assertEquals(expenseRecurring, result);
        verify(expenseRecurringRepository).findById(expenseRecurringId);
    }

    // Teste para exceção: deve lançar DatabaseValueNotFoundException quando a despesa recorrente não for encontrada
    @Test
    void getExpenseRecurring_deveLancarExcecao_quandoDespesaRecorrenteNaoEncontrada() {
        // Given
        Long expenseRecurringId = 2L;
        User user = mock(User.class);
        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.empty());

        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getExpenseRecurringCase.execute(user, expenseRecurringId)
        );
        // Valida os detalhes da exceção (chave correta: "valor")
        assertNotNull(ex.getDetails());
        assertEquals(expenseRecurringId.toString(), ex.getDetails().get("valor"));
    }

    // Teste para exceção: deve lançar DatabaseValueNotFoundException quando a despesa recorrente não pertence ao usuário
    @Test
    void getExpenseRecurring_deveLancarExcecao_quandoDespesaRecorrenteNaoPertenceAoUsuario() {
        // Given
        Long expenseRecurringId = 3L;
        Long userId = 20L;
        Long otherUserId = 99L;
        User user = mock(User.class);
        ExpenseRecurring expenseRecurring = mock(ExpenseRecurring.class);
        when(user.getId()).thenReturn(userId);
        when(expenseRecurring.getUserId()).thenReturn(otherUserId);
        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));

        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getExpenseRecurringCase.execute(user, expenseRecurringId)
        );
        // Valida os detalhes da exceção (chave correta: "valor")
        assertNotNull(ex.getDetails());
        assertEquals(expenseRecurringId.toString(), ex.getDetails().get("valor"));
    }
}
