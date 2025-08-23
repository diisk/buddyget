package br.dev.diisk.application.finance.expense_transaction.cases;

import br.dev.diisk.application.finance.ListRecurringReferenceDates;
import br.dev.diisk.application.finance.expense_transaction.dtos.PendingExpenseTransactionDTO;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurringFixture;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransactionFixture;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para ListPendingExpenseTransactionsCase.
 */
@ExtendWith(MockitoExtension.class)
class ListPendingExpenseTransactionsCaseTest {

    @Mock
    private IExpenseTransactionRepository expenseRepository;

    @Mock
    private IExpenseRecurringRepository expenseRecurringRepository;

    @Mock
    private ListRecurringReferenceDates listRecurringReferenceDates;

    @InjectMocks
    private ListPendingExpenseTransactionsCase listPendingExpenseTransactionsCase;

    @Test
    @DisplayName("Deve retornar lista com transações pendentes do usuário quando existem transações pendentes")
    void listPendingExpenseTransactions_deveRetornarListaComTransacoesPendentes_quandoExistemTransacoesPendentes() {
        // Given - prepara os mocks e dados de entrada
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        
        ExpenseTransaction pendingTransaction = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        
        List<ExpenseTransaction> pendingTransactions = Arrays.asList(pendingTransaction);
        List<ExpenseRecurring> activeRecurrings = new ArrayList<>();
        List<ExpenseTransaction> recurringRelatedTransactions = new ArrayList<>();

        when(expenseRepository.findAllPendingBy(user.getId())).thenReturn(pendingTransactions);
        when(expenseRecurringRepository.findAllActiveBy(user.getId())).thenReturn(activeRecurrings);
        when(expenseRepository.findAllRecurringRelatedBy(anyList())).thenReturn(recurringRelatedTransactions);

        // When - executa o método a ser testado
        List<PendingExpenseTransactionDTO> result = listPendingExpenseTransactionsCase.execute(user);

        // Then - verifica os resultados usando assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pendingTransaction.getId(), result.get(0).id());
        assertEquals(pendingTransaction.getDescription(), result.get(0).description());
        assertEquals(pendingTransaction.getValue(), result.get(0).value());
        assertEquals(pendingTransaction.getCategory(), result.get(0).category());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem transações pendentes nem recorrências ativas")
    void listPendingExpenseTransactions_deveRetornarListaVazia_quandoNaoExistemTransacoesPendentesNemRecorrenciasAtivas() {
        // Given - prepara os mocks e dados de entrada
        User user = UserFixture.umUsuarioComId(1L);
        
        List<ExpenseTransaction> pendingTransactions = new ArrayList<>();
        List<ExpenseRecurring> activeRecurrings = new ArrayList<>();
        List<ExpenseTransaction> recurringRelatedTransactions = new ArrayList<>();

        when(expenseRepository.findAllPendingBy(user.getId())).thenReturn(pendingTransactions);
        when(expenseRecurringRepository.findAllActiveBy(user.getId())).thenReturn(activeRecurrings);
        when(expenseRepository.findAllRecurringRelatedBy(anyList())).thenReturn(recurringRelatedTransactions);

        // When - executa o método a ser testado
        List<PendingExpenseTransactionDTO> result = listPendingExpenseTransactionsCase.execute(user);

        // Then - verifica os resultados usando assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve incluir transações de recorrências ativas quando há datas de referência sem transações correspondentes")
    void listPendingExpenseTransactions_deveIncluirTransacoesDeRecorrenciasAtivas_quandoHaDatasDeReferenciaSemTransacoesCorrespondentes() {
        // Given - prepara os mocks e dados de entrada
        User user = UserFixture.umUsuarioComId(1L);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(1L, user);
        
        List<ExpenseTransaction> pendingTransactions = new ArrayList<>();
        List<ExpenseRecurring> activeRecurrings = Arrays.asList(expenseRecurring);
        List<ExpenseTransaction> recurringRelatedTransactions = new ArrayList<>();
        
        LocalDateTime referenceDate1 = LocalDateTime.now().minusMonths(1);
        LocalDateTime referenceDate2 = LocalDateTime.now();
        List<LocalDateTime> referenceDates = Arrays.asList(referenceDate1, referenceDate2);

        when(expenseRepository.findAllPendingBy(user.getId())).thenReturn(pendingTransactions);
        when(expenseRecurringRepository.findAllActiveBy(user.getId())).thenReturn(activeRecurrings);
        when(expenseRepository.findAllRecurringRelatedBy(Arrays.asList(expenseRecurring.getId()))).thenReturn(recurringRelatedTransactions);
        when(listRecurringReferenceDates.execute(expenseRecurring)).thenReturn(referenceDates);

        // When - executa o método a ser testado
        List<PendingExpenseTransactionDTO> result = listPendingExpenseTransactionsCase.execute(user);

        // Then - verifica os resultados usando assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verifica se as transações geradas possuem os dados corretos da recorrência
        for (PendingExpenseTransactionDTO dto : result) {
            assertNull(dto.id());
            assertEquals(expenseRecurring, dto.recurring());
            assertEquals(expenseRecurring.getDescription(), dto.description());
            assertEquals(expenseRecurring.getValue(), dto.value());
            assertEquals(expenseRecurring.getCategory(), dto.category());
            assertEquals(expenseRecurring.getCreditCard(), dto.creditCard());
            assertEquals(expenseRecurring.getWishItem(), dto.wishitem());
        }
    }

    @Test
    @DisplayName("Deve não incluir transações de recorrências quando já existem transações para todas as datas de referência")
    void listPendingExpenseTransactions_deveNaoIncluirTransacoesDeRecorrencias_quandoJaExistemTransacoesParaTodasAsDatasDeReferencia() {
        // Given - prepara os mocks e dados de entrada
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(1L, user);
        
        LocalDateTime referenceDate = LocalDateTime.now();
        
        ExpenseTransaction relatedTransaction = ExpenseTransactionFixture.umaTransacaoComExpenseRecurring(
                2L, user, category, expenseRecurring, referenceDate);
        
        List<ExpenseTransaction> pendingTransactions = new ArrayList<>();
        List<ExpenseRecurring> activeRecurrings = Arrays.asList(expenseRecurring);
        List<ExpenseTransaction> recurringRelatedTransactions = Arrays.asList(relatedTransaction);
        List<LocalDateTime> referenceDates = Arrays.asList(referenceDate);

        when(expenseRepository.findAllPendingBy(user.getId())).thenReturn(pendingTransactions);
        when(expenseRecurringRepository.findAllActiveBy(user.getId())).thenReturn(activeRecurrings);
        when(expenseRepository.findAllRecurringRelatedBy(Arrays.asList(expenseRecurring.getId()))).thenReturn(recurringRelatedTransactions);
        when(listRecurringReferenceDates.execute(expenseRecurring)).thenReturn(referenceDates);

        // When - executa o método a ser testado
        List<PendingExpenseTransactionDTO> result = listPendingExpenseTransactionsCase.execute(user);

        // Then - verifica os resultados usando assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve ordenar as transações por data de vencimento crescente quando há transações com datas de vencimento")
    void listPendingExpenseTransactions_deveOrdenarTransacoesPorDataDeVencimentoCrescente_quandoHaTransacoesComDatasDeVencimento() {
        // Given - prepara os mocks e dados de entrada
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        
        ExpenseTransaction transaction1 = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        transaction1.addDueDate(LocalDateTime.now().plusDays(2));
        
        ExpenseTransaction transaction2 = ExpenseTransactionFixture.umaTransacaoComId(2L, user, category);
        transaction2.addDueDate(LocalDateTime.now().plusDays(1));
        
        List<ExpenseTransaction> pendingTransactions = Arrays.asList(transaction1, transaction2);
        List<ExpenseRecurring> activeRecurrings = new ArrayList<>();
        List<ExpenseTransaction> recurringRelatedTransactions = new ArrayList<>();

        when(expenseRepository.findAllPendingBy(user.getId())).thenReturn(pendingTransactions);
        when(expenseRecurringRepository.findAllActiveBy(user.getId())).thenReturn(activeRecurrings);
        when(expenseRepository.findAllRecurringRelatedBy(anyList())).thenReturn(recurringRelatedTransactions);

        // When - executa o método a ser testado
        List<PendingExpenseTransactionDTO> result = listPendingExpenseTransactionsCase.execute(user);

        // Then - verifica os resultados usando assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        // Verifica se a transação com data de vencimento mais próxima vem primeiro
        assertEquals(transaction2.getId(), result.get(0).id());
        assertEquals(transaction1.getId(), result.get(1).id());
    }

    @Test
    @DisplayName("Deve colocar transações sem data de vencimento no final da lista quando há transações com e sem datas de vencimento")
    void listPendingExpenseTransactions_deveColocarTransacoesSemDataDeVencimentoNoFinalDaLista_quandoHaTransacoesComESemDatasDeVencimento() {
        // Given - prepara os mocks e dados de entrada
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        
        ExpenseTransaction transactionWithDueDate = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        transactionWithDueDate.addDueDate(LocalDateTime.now().plusDays(1));
        
        ExpenseTransaction transactionWithoutDueDate = ExpenseTransactionFixture.umaTransacaoComId(2L, user, category);
        
        List<ExpenseTransaction> pendingTransactions = Arrays.asList(transactionWithoutDueDate, transactionWithDueDate);
        List<ExpenseRecurring> activeRecurrings = new ArrayList<>();
        List<ExpenseTransaction> recurringRelatedTransactions = new ArrayList<>();

        when(expenseRepository.findAllPendingBy(user.getId())).thenReturn(pendingTransactions);
        when(expenseRecurringRepository.findAllActiveBy(user.getId())).thenReturn(activeRecurrings);
        when(expenseRepository.findAllRecurringRelatedBy(anyList())).thenReturn(recurringRelatedTransactions);

        // When - executa o método a ser testado
        List<PendingExpenseTransactionDTO> result = listPendingExpenseTransactionsCase.execute(user);

        // Then - verifica os resultados usando assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        // Verifica se a transação com data de vencimento vem primeiro
        assertEquals(transactionWithDueDate.getId(), result.get(0).id());
        assertEquals(transactionWithoutDueDate.getId(), result.get(1).id());
    }

    @Test
    @DisplayName("Deve filtrar transações relacionadas somente para a recorrência específica quando há múltiplas recorrências")
    void listPendingExpenseTransactions_deveFiltrarTransacoesRelacionadasSomenteParaARecorrenciaEspecifica_quandoHaMultiplasRecorrencias() {
        // Given - prepara os mocks e dados de entrada
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        
        ExpenseRecurring expenseRecurring1 = ExpenseRecurringFixture.umaExpenseRecurringComId(1L, user);
        ExpenseRecurring expenseRecurring2 = ExpenseRecurringFixture.umaExpenseRecurringComId(2L, user);
        
        LocalDateTime referenceDate = LocalDateTime.of(2024, 6, 1, 0, 0, 0);
        
        ExpenseTransaction relatedTransaction1 = ExpenseTransactionFixture.umaTransacaoComExpenseRecurring(
                1L, user, category, expenseRecurring1, referenceDate);
        
        ExpenseTransaction relatedTransaction2 = ExpenseTransactionFixture.umaTransacaoComExpenseRecurring(
                2L, user, category, expenseRecurring2, referenceDate);
        
        List<ExpenseTransaction> pendingTransactions = new ArrayList<>();
        List<ExpenseRecurring> activeRecurrings = Arrays.asList(expenseRecurring1, expenseRecurring2);
        List<ExpenseTransaction> recurringRelatedTransactions = Arrays.asList(relatedTransaction1, relatedTransaction2);
        List<LocalDateTime> referenceDates = Arrays.asList(referenceDate);

        when(expenseRepository.findAllPendingBy(user.getId())).thenReturn(pendingTransactions);
        when(expenseRecurringRepository.findAllActiveBy(user.getId())).thenReturn(activeRecurrings);
        when(expenseRepository.findAllRecurringRelatedBy(Arrays.asList(1L, 2L))).thenReturn(recurringRelatedTransactions);
        when(listRecurringReferenceDates.execute(any(ExpenseRecurring.class))).thenReturn(referenceDates);

        // When - executa o método a ser testado
        List<PendingExpenseTransactionDTO> result = listPendingExpenseTransactionsCase.execute(user);

        // Then - verifica os resultados usando assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve incluir transações de recorrências quando transações relacionadas pertencem a outras recorrências")
    void listPendingExpenseTransactions_deveIncluirTransacoesDeRecorrencias_quandoTransacoesRelacionadasPertencemAOutrasRecorrencias() {
        // Given - prepara os mocks e dados de entrada
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        
        ExpenseRecurring expenseRecurring1 = ExpenseRecurringFixture.umaExpenseRecurringComId(1L, user);
        ExpenseRecurring expenseRecurring2 = ExpenseRecurringFixture.umaExpenseRecurringComId(2L, user);
        
        // Transação relacionada à recorrência 2, mas processando recorrência 1
        ExpenseTransaction relatedTransaction = ExpenseTransactionFixture.umaTransacaoComExpenseRecurring(
                1L, user, category, expenseRecurring2, LocalDateTime.now());
        
        List<ExpenseTransaction> pendingTransactions = new ArrayList<>();
        List<ExpenseRecurring> activeRecurrings = Arrays.asList(expenseRecurring1);
        List<ExpenseTransaction> recurringRelatedTransactions = Arrays.asList(relatedTransaction);
        List<LocalDateTime> referenceDates = Arrays.asList(LocalDateTime.now());

        when(expenseRepository.findAllPendingBy(user.getId())).thenReturn(pendingTransactions);
        when(expenseRecurringRepository.findAllActiveBy(user.getId())).thenReturn(activeRecurrings);
        when(expenseRepository.findAllRecurringRelatedBy(Arrays.asList(1L))).thenReturn(recurringRelatedTransactions);
        when(listRecurringReferenceDates.execute(expenseRecurring1)).thenReturn(referenceDates);

        // When - executa o método a ser testado
        List<PendingExpenseTransactionDTO> result = listPendingExpenseTransactionsCase.execute(user);

        // Then - verifica os resultados usando assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expenseRecurring1, result.get(0).recurring());
    }
}
