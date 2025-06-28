package br.dev.diisk.application.finance.expense_transaction.cases;

import br.dev.diisk.application.finance.expense_transaction.dtos.UpdateExpenseTransactionParams;
import br.dev.diisk.application.monthly_summary.cases.UpdateMonthlySummaryCase;
import br.dev.diisk.application.monthly_summary.dtos.UpdateMonthlySummaryParams;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransactionFixture;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Teste de caminho feliz para UpdateExpenseTransactionCase.
 * Garante que a transação de despesa é atualizada corretamente e que o resumo mensal é chamado.
 */
@ExtendWith(MockitoExtension.class)
class UpdateExpenseTransactionCaseTest {

    @Mock
    private IExpenseTransactionRepository expenseRepository;
    @Mock
    private GetExpenseTransactionCase getExpenseTransactionCase;
    @Mock
    private UpdateMonthlySummaryCase updateMonthlySummaryCase;

    @InjectMocks
    private UpdateExpenseTransactionCase updateExpenseTransactionCase;

    @Test
    void updateExpenseTransaction_deveAtualizarDespesaEChamarResumoMensal_quandoDadosValidos() {
        // Given: mocks e dados de entrada reais
        User user = UserFixture.umUsuarioComId(10L);
        Category category = CategoryFixture.umaCategoriaComId(20L, CategoryTypeEnum.EXPENSE, user);
        ExpenseTransaction expenseTransaction = ExpenseTransactionFixture.umaTransacaoComId(30L, user, category);
        Long expenseTransactionId = 30L;
        UpdateExpenseTransactionParams params = mock(UpdateExpenseTransactionParams.class);

        BigDecimal previousValue = expenseTransaction.getValue();
        LocalDateTime previousPaymentDate = expenseTransaction.getPaymentDate();
        BigDecimal newValue = new BigDecimal("150.00");
        LocalDateTime newPaymentDate = LocalDateTime.of(2024, 7, 1, 12, 0);
        LocalDateTime newDueDate = LocalDateTime.of(2024, 7, 10, 12, 0);

        when(getExpenseTransactionCase.execute(user, expenseTransactionId)).thenReturn(expenseTransaction);
        when(params.getDescription()).thenReturn("Nova descrição");
        when(params.getValue()).thenReturn(newValue);
        when(params.getPaymentDate()).thenReturn(newPaymentDate);
        when(params.getDueDate()).thenReturn(newDueDate);

        // When: executa o método alvo
        ExpenseTransaction result = updateExpenseTransactionCase.execute(user, expenseTransactionId, params);

        // Then: verifica se os métodos corretos foram chamados e o retorno está correto
        verify(getExpenseTransactionCase).execute(user, expenseTransactionId);
        verify(expenseRepository).save(expenseTransaction);
        verify(updateMonthlySummaryCase).execute(eq(user), any(UpdateMonthlySummaryParams.class));
        assertEquals(expenseTransaction, result);

        // Verifica se os valores da entidade foram atualizados corretamente
        assertEquals("Nova descrição", expenseTransaction.getDescription());
        assertEquals(newValue, expenseTransaction.getValue());
        assertEquals(newPaymentDate, expenseTransaction.getPaymentDate());
        assertEquals(newDueDate, expenseTransaction.getDueDate());

        // Captura os parâmetros do resumo mensal para validar os valores
        ArgumentCaptor<UpdateMonthlySummaryParams> captor = ArgumentCaptor.forClass(UpdateMonthlySummaryParams.class);
        verify(updateMonthlySummaryCase).execute(eq(user), captor.capture());
        UpdateMonthlySummaryParams summaryParams = captor.getValue();
        assertEquals(previousValue, summaryParams.getPreviousValue());
        assertEquals(previousPaymentDate, summaryParams.getPreviousDate());
        assertEquals(newValue, summaryParams.getNewValue());
        assertEquals(newPaymentDate, summaryParams.getNewDate());
        assertEquals(category, summaryParams.getCategory());
    }
}
