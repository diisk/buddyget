package br.dev.diisk.application.finance.expense_transaction.cases;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
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
 * Testes unitários para o caso de uso GetExpenseTransactionCase.
 */
@ExtendWith(MockitoExtension.class)
class GetExpenseTransactionCaseTest {

    @Mock
    private IExpenseTransactionRepository expenseRepository;

    @InjectMocks
    private GetExpenseTransactionCase getExpenseTransactionCase;

    // Teste para o caminho feliz: deve retornar a transação quando encontrada e pertence ao usuário
    @Test
    void getExpenseTransaction_deveRetornarTransacao_quandoEncontradaEUsuarioCorreto() {
        // Given
        Long expenseTransactionId = 1L;
        Long userId = 10L;
        User user = mock(User.class);
        ExpenseTransaction expenseTransaction = mock(ExpenseTransaction.class);
        when(user.getId()).thenReturn(userId);
        when(expenseTransaction.getUserId()).thenReturn(userId);
        when(expenseRepository.findById(expenseTransactionId)).thenReturn(Optional.of(expenseTransaction));

        // When
        ExpenseTransaction result = getExpenseTransactionCase.execute(user, expenseTransactionId);

        // Then
        assertEquals(expenseTransaction, result);
        verify(expenseRepository).findById(expenseTransactionId);
    }

    // Teste para exceção: deve lançar DatabaseValueNotFoundException quando a transação não for encontrada
    @Test
    void getExpenseTransaction_deveLancarExcecao_quandoTransacaoNaoEncontrada() {
        // Given
        Long expenseTransactionId = 2L;
        User user = mock(User.class);
        when(expenseRepository.findById(expenseTransactionId)).thenReturn(Optional.empty());

        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getExpenseTransactionCase.execute(user, expenseTransactionId)
        );
        // Valida os detalhes da exceção (chave correta: "valor")
        assertNotNull(ex.getDetails());
        assertEquals(expenseTransactionId.toString(), ex.getDetails().get("valor"));
    }

    // Teste para exceção: deve lançar DatabaseValueNotFoundException quando a transação não pertence ao usuário
    @Test
    void getExpenseTransaction_deveLancarExcecao_quandoTransacaoNaoPertenceAoUsuario() {
        // Given
        Long expenseTransactionId = 3L;
        Long userId = 20L;
        Long otherUserId = 99L;
        User user = mock(User.class);
        ExpenseTransaction expenseTransaction = mock(ExpenseTransaction.class);
        when(user.getId()).thenReturn(userId);
        when(expenseTransaction.getUserId()).thenReturn(otherUserId);
        when(expenseRepository.findById(expenseTransactionId)).thenReturn(Optional.of(expenseTransaction));

        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getExpenseTransactionCase.execute(user, expenseTransactionId)
        );
        // Valida os detalhes da exceção (chave correta: "valor")
        assertNotNull(ex.getDetails());
        assertEquals(expenseTransactionId.toString(), ex.getDetails().get("valor"));
    }
}
