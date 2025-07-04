package br.dev.diisk.application.finance.expense_transaction.cases;

import br.dev.diisk.application.monthly_summary.cases.RemoveMonthlySummaryValueCase;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransactionFixture;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para DeleteExpenseTransactionCase.
 */
@ExtendWith(MockitoExtension.class)
class DeleteExpenseTransactionCaseTest {

    @Mock
    private IExpenseTransactionRepository expenseRepository;
    @Mock
    private RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;
    @InjectMocks
    private DeleteExpenseTransactionCase deleteExpenseTransactionCase;

    // Teste: Deve deletar a transação de despesa e remover do resumo mensal quando existir e pertencer ao usuário
    @Test
    void deleteExpenseTransaction_deveDeletarEAtualizarResumo_quandoTransacaoExisteEPertenceAoUsuario() {
        // Given
        Long userId = 1L;
        Long expenseId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(100L, CategoryTypeEnum.EXPENSE, user);
        ExpenseTransaction expense = ExpenseTransactionFixture.umaTransacaoComId(expenseId, user, category);
        // Customizando valor e data
        BigDecimal value = BigDecimal.TEN;
        LocalDateTime paymentDate = LocalDateTime.of(2024, 6, 1, 0, 0);
        expense.update(expense.getDescription(), value, paymentDate, null);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // When
        deleteExpenseTransactionCase.execute(user, expenseId);

        // Then
        verify(expenseRepository).save(expense);
        verify(removeMonthlySummaryValueCase).execute(eq(user), any());
        assertTrue(expense.isDeleted());
    }

    // Teste: Não deve fazer nada se a transação não existir
    @Test
    void deleteExpenseTransaction_naoFazNada_quandoTransacaoNaoExiste() {
        // Given
        Long userId = 1L;
        Long expenseId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        // When
        deleteExpenseTransactionCase.execute(user, expenseId);

        // Then
        verify(expenseRepository, never()).save(any(ExpenseTransaction.class));
        verify(removeMonthlySummaryValueCase, never()).execute(any(), any());
    }

    // Teste: Não deve fazer nada se a transação não pertencer ao usuário
    @Test
    void deleteExpenseTransaction_naoFazNada_quandoTransacaoNaoPertenceAoUsuario() {
        // Given
        Long userId = 1L;
        Long expenseId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        User outroUser = UserFixture.umUsuarioComId(999L);
        Category category = CategoryFixture.umaCategoriaComId(100L, CategoryTypeEnum.EXPENSE, outroUser);
        ExpenseTransaction expense = ExpenseTransactionFixture.umaTransacaoComId(expenseId, outroUser, category);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // When
        deleteExpenseTransactionCase.execute(user, expenseId);

        // Then
        verify(expenseRepository, never()).save(any(ExpenseTransaction.class));
        verify(removeMonthlySummaryValueCase, never()).execute(any(), any());
    }

    // Teste: Deve deletar a transação mas não chamar o resumo mensal se não houver paymentDate
    @Test
    void deleteExpenseTransaction_deveDeletarSemResumo_quandoNaoTemPaymentDate() {
        // Given
        Long userId = 1L;
        Long expenseId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(100L, CategoryTypeEnum.EXPENSE, user);
        ExpenseTransaction expense = ExpenseTransactionFixture.umaTransacaoComId(expenseId, user, category);
        // Remove a data de pagamento
        expense.update(expense.getDescription(), expense.getValue(), null, null);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // When
        deleteExpenseTransactionCase.execute(user, expenseId);

        // Then
        verify(expenseRepository).save(expense);
        verify(removeMonthlySummaryValueCase, never()).execute(any(), any());
        assertTrue(expense.isDeleted());
    }
}
