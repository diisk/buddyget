package br.dev.diisk.application.finance.expense_recurring.cases;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.application.finance.AdjustRecurringCase;
import br.dev.diisk.domain.finance.Transaction;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurringFixture;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransactionFixture;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import br.dev.diisk.infra.finance.expense_recurring.ExpenseRecurringRepository;
import br.dev.diisk.infra.finance.expense_transaction.ExpenseTransactionRepository;

@ExtendWith(MockitoExtension.class)
class AdjustExpenseRecurringCaseTest {

    @Mock
    private AdjustRecurringCase adjustRecurringCase;

    @Mock
    private ExpenseTransactionRepository expenseTransactionRepository;

    @Mock
    private ExpenseRecurringRepository expenseRecurringRepository;

    @InjectMocks
    private AdjustExpenseRecurringCase adjustExpenseRecurringCase;

    @Test
    void adjustExpenseRecurring_deveChamarAdjustRecurringCaseComParametrosCorretos_quandoExpenseRecurringValido() {
        // Given - preparar os dados de entrada e mocks
        User user = UserFixture.umUsuarioComId(1L);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(1L, user);
        
        ExpenseTransaction expenseTransaction1 = ExpenseTransactionFixture.umaTransacaoComId(1L, user, expenseRecurring.getCategory());
        ExpenseTransaction expenseTransaction2 = ExpenseTransactionFixture.umaTransacaoComId(2L, user, expenseRecurring.getCategory());
        List<ExpenseTransaction> expenseTransactions = Arrays.asList(expenseTransaction1, expenseTransaction2);
        
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurring.getId())))
                .thenReturn(expenseTransactions);

        // When - executar o método a ser testado
        adjustExpenseRecurringCase.execute(expenseRecurring);

        // Then - verificar os resultados usando assertions
        verify(expenseTransactionRepository).findAllRecurringRelatedBy(List.of(expenseRecurring.getId()));
        verify(adjustRecurringCase).execute(eq(expenseRecurring), anyList(), any());
        verifyNoMoreInteractions(adjustRecurringCase, expenseTransactionRepository);
    }

    @Test
    void adjustExpenseRecurring_devePassarListaVaziaDeTransacoes_quandoNaoExistemTransacoesRelacionadas() {
        // Given - preparar os dados com lista vazia de transações
        User user = UserFixture.umUsuarioComId(1L);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(1L, user);
        
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurring.getId())))
                .thenReturn(List.of());

        // When - executar o método a ser testado
        adjustExpenseRecurringCase.execute(expenseRecurring);

        // Then - verificar que o adjustRecurringCase foi chamado com lista vazia
        verify(expenseTransactionRepository).findAllRecurringRelatedBy(List.of(expenseRecurring.getId()));
        verify(adjustRecurringCase).execute(eq(expenseRecurring), eq(List.of()), any());
        verifyNoMoreInteractions(adjustRecurringCase, expenseTransactionRepository);
    }

    @Test
    void adjustExpenseRecurring_deveConverterExpenseTransactionsParaTransactions_quandoExistemMultiplasTransacoes() {
        // Given - preparar múltiplas transações para verificar a conversão
        User user = UserFixture.umUsuarioComId(1L);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(1L, user);
        
        ExpenseTransaction expenseTransaction1 = ExpenseTransactionFixture.umaTransacaoComId(1L, user, expenseRecurring.getCategory());
        ExpenseTransaction expenseTransaction2 = ExpenseTransactionFixture.umaTransacaoComId(2L, user, expenseRecurring.getCategory());
        ExpenseTransaction expenseTransaction3 = ExpenseTransactionFixture.umaTransacaoComId(3L, user, expenseRecurring.getCategory());
        List<ExpenseTransaction> expenseTransactions = Arrays.asList(expenseTransaction1, expenseTransaction2, expenseTransaction3);
        
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurring.getId())))
                .thenReturn(expenseTransactions);

        // When - executar o método a ser testado
        adjustExpenseRecurringCase.execute(expenseRecurring);

        // Then - verificar que todas as transações foram convertidas para Transaction
        verify(adjustRecurringCase).execute(eq(expenseRecurring), argThat(transactions -> {
            List<Transaction> transactionList = (List<Transaction>) transactions;
            return transactionList.size() == 3 &&
                   transactionList.stream().allMatch(t -> t instanceof Transaction);
        }), any());
    }
}
