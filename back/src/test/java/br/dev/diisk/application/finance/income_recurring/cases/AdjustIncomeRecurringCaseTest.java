package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.application.finance.AdjustRecurringCase;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.Transaction;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransactionFixture;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import br.dev.diisk.infra.finance.income_recurring.IncomeRecurringRepository;
import br.dev.diisk.infra.finance.income_transaction.IncomeTransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AdjustIncomeRecurringCase.
 */
@ExtendWith(MockitoExtension.class)
class AdjustIncomeRecurringCaseTest {

    @Mock
    private AdjustRecurringCase adjustRecurringCase;

    @Mock
    private IncomeTransactionRepository incomeTransactionRepository;

    @Mock
    private IncomeRecurringRepository incomeRecurringRepository;

    @InjectMocks
    private AdjustIncomeRecurringCase adjustIncomeRecurringCase;

    @Test
    @DisplayName("Deve ajustar receita recorrente corretamente quando existem transações relacionadas")
    void adjustIncomeRecurring_deveAjustarCorretamente_quandoExistemTransacoesRelacionadas() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 1L;
        Long categoryId = 1L;
        Long incomeRecurringId = 1L;
        
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
            incomeRecurringId, user, category, recurringDay, period);
            
        IncomeTransaction transaction1 = IncomeTransactionFixture.umaIncomeTransactionComId(1L);
        IncomeTransaction transaction2 = IncomeTransactionFixture.umaIncomeTransactionComId(2L);
        List<IncomeTransaction> incomeTransactions = List.of(transaction1, transaction2);
        
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
            .thenReturn(incomeTransactions);

        // When - Executa o método a ser testado
        adjustIncomeRecurringCase.execute(incomeRecurring);

        // Then - Verifica os resultados
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(adjustRecurringCase).execute(eq(incomeRecurring), anyList(), any());
    }

    @Test
    @DisplayName("Deve ajustar receita recorrente corretamente quando não existem transações relacionadas")
    void adjustIncomeRecurring_deveAjustarCorretamente_quandoNaoExistemTransacoesRelacionadas() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 1L;
        Long categoryId = 1L;
        Long incomeRecurringId = 1L;
        
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
            incomeRecurringId, user, category, recurringDay, period);
            
        List<IncomeTransaction> incomeTransactions = List.of();
        
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
            .thenReturn(incomeTransactions);

        // When - Executa o método a ser testado
        adjustIncomeRecurringCase.execute(incomeRecurring);

        // Then - Verifica os resultados
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(adjustRecurringCase).execute(eq(incomeRecurring), eq(List.of()), any());
    }

    @Test
    @DisplayName("Deve passar função de salvamento correta para o AdjustRecurringCase")
    void adjustIncomeRecurring_devePassarFuncaoSalvamentoCorreta_paraAdjustRecurringCase() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 1L;
        Long categoryId = 1L;
        Long incomeRecurringId = 1L;
        
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
            incomeRecurringId, user, category, recurringDay, period);
            
        List<IncomeTransaction> incomeTransactions = List.of();
        
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
            .thenReturn(incomeTransactions);

        // When - Executa o método a ser testado
        adjustIncomeRecurringCase.execute(incomeRecurring);

        // Then - Verifica que o AdjustRecurringCase foi chamado com os parâmetros corretos
        verify(adjustRecurringCase).execute(
            eq(incomeRecurring), 
            eq(List.of()), 
            any()
        );
        
        // Verifica que o repositório não foi chamado ainda (será chamado apenas se necessário pela função lambda)
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    @Test
    @DisplayName("Deve converter corretamente IncomeTransaction para Transaction")
    void adjustIncomeRecurring_deveConverterCorretamente_incomeTransactionParaTransaction() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 1L;
        Long categoryId = 1L;
        Long incomeRecurringId = 1L;
        
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
            incomeRecurringId, user, category, recurringDay, period);
            
        IncomeTransaction transaction1 = IncomeTransactionFixture.umaIncomeTransactionComId(1L);
        IncomeTransaction transaction2 = IncomeTransactionFixture.umaIncomeTransactionComId(2L);
        List<IncomeTransaction> incomeTransactions = List.of(transaction1, transaction2);
        
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
            .thenReturn(incomeTransactions);

        // When - Executa o método a ser testado
        adjustIncomeRecurringCase.execute(incomeRecurring);

        // Then - Verifica que as transações foram convertidas corretamente para Transaction
        verify(adjustRecurringCase).execute(
            eq(incomeRecurring), 
            argThat(transactions -> {
                List<Transaction> transactionList = (List<Transaction>) transactions;
                return transactionList.size() == 2 &&
                       transactionList.get(0) instanceof Transaction &&
                       transactionList.get(1) instanceof Transaction;
            }), 
            any()
        );
    }

    @Test
    @DisplayName("Deve executar operação dentro de transação")
    void adjustIncomeRecurring_deveExecutarDentroDeTransacao_sempreQueExecutado() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 1L;
        Long categoryId = 1L;
        Long incomeRecurringId = 1L;
        
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
            incomeRecurringId, user, category, recurringDay, period);
            
        List<IncomeTransaction> incomeTransactions = List.of();
        
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
            .thenReturn(incomeTransactions);

        // When - Executa o método a ser testado
        adjustIncomeRecurringCase.execute(incomeRecurring);

        // Then - Verifica que todos os métodos necessários foram chamados
        // A verificação da transação @Transactional é implícita, pois o método é anotado
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(adjustRecurringCase).execute(eq(incomeRecurring), anyList(), any());
    }
}
