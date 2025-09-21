package br.dev.diisk.application.finance;

import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.Recurring;
import br.dev.diisk.domain.finance.Transaction;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransactionFixture;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso AdjustRecurring.
 * Segue o padrão Given-When-Then e cobre cenários felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class AdjustRecurringCaseTest {

    @Mock
    private UtilService utilService;

    @Mock
    private ListRecurringReferenceDatesCase listRecurringReferenceDatesCase;

    @InjectMocks
    private AdjustRecurringCase adjustRecurringCase;

    /**
     * Deve manter status ativo quando há transações pendentes em pelo menos uma data de referência.
     */
    @Test
    void adjustRecurring_deveManterStatusAtivo_quandoHaTransacoesPendentesEmPeloMenosUmaDataReferencia() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring recurring = criarExpenseRecurringAtiva(1L, user);
        
        LocalDateTime dataReferencia1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dataReferencia2 = LocalDateTime.of(2024, 2, 1, 0, 0);
        List<LocalDateTime> referenceDates = Arrays.asList(dataReferencia1, dataReferencia2);
        
        ExpenseTransaction transacaoComPagamento = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        List<Transaction> relatedTransactions = Collections.singletonList(transacaoComPagamento);
        
        @SuppressWarnings("unchecked")
        Function<Recurring, Void> saveFunction = mock(Function.class);
        
        when(listRecurringReferenceDatesCase.execute(recurring)).thenReturn(referenceDates);
        when(utilService.isReferenceDateEquals(dataReferencia1, transacaoComPagamento.getRecurringReferenceDate())).thenReturn(false);

        // When
        adjustRecurringCase.execute(recurring, relatedTransactions, saveFunction);

        // Then
        verify(listRecurringReferenceDatesCase).execute(recurring);
        verify(utilService).isReferenceDateEquals(dataReferencia1, transacaoComPagamento.getRecurringReferenceDate());
        // Note: anyMatch para na primeira ocorrência que retorna true, então não verifica a segunda data
        verifyNoInteractions(saveFunction); // Status não muda pois já é ativo
    }

    /**
     * Deve inativar recorrência quando todas as datas de referência possuem transações pagas.
     */
    @Test
    void adjustRecurring_deveInativarRecorrencia_quandoTodasDatasReferenciasPossuemTransacoesPagas() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring recurring = criarExpenseRecurringAtiva(1L, user);
        
        LocalDateTime dataReferencia1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dataReferencia2 = LocalDateTime.of(2024, 2, 1, 0, 0);
        List<LocalDateTime> referenceDates = Arrays.asList(dataReferencia1, dataReferencia2);
        
        ExpenseTransaction transacao1 = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        ExpenseTransaction transacao2 = ExpenseTransactionFixture.umaTransacaoComId(2L, user, category);
        List<Transaction> relatedTransactions = Arrays.asList(transacao1, transacao2);
        
        @SuppressWarnings("unchecked")
        Function<Recurring, Void> saveFunction = mock(Function.class);
        
        when(listRecurringReferenceDatesCase.execute(recurring)).thenReturn(referenceDates);
        when(utilService.isReferenceDateEquals(dataReferencia1, transacao1.getRecurringReferenceDate())).thenReturn(true);
        when(utilService.isReferenceDateEquals(dataReferencia2, transacao2.getRecurringReferenceDate())).thenReturn(true);

        // When
        adjustRecurringCase.execute(recurring, relatedTransactions, saveFunction);

        // Then
        verify(listRecurringReferenceDatesCase).execute(recurring);
        verify(utilService).isReferenceDateEquals(dataReferencia1, transacao1.getRecurringReferenceDate());
        verify(utilService).isReferenceDateEquals(dataReferencia1, transacao2.getRecurringReferenceDate());
        verify(saveFunction).apply(recurring);
    }

    /**
     * Deve ativar recorrência quando há pelo menos uma data de referência sem transação paga.
     */
    @Test
    void adjustRecurring_deveAtivarRecorrencia_quandoHaPeloMenosUmaDataReferenciaSemTransacaoPaga() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring recurring = criarExpenseRecurringInativa(1L, user);
        
        LocalDateTime dataReferencia1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dataReferencia2 = LocalDateTime.of(2024, 2, 1, 0, 0);
        List<LocalDateTime> referenceDates = Arrays.asList(dataReferencia1, dataReferencia2);
        
        ExpenseTransaction transacaoComPagamento = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        List<Transaction> relatedTransactions = Collections.singletonList(transacaoComPagamento);
        
        @SuppressWarnings("unchecked")
        Function<Recurring, Void> saveFunction = mock(Function.class);
        
        when(listRecurringReferenceDatesCase.execute(recurring)).thenReturn(referenceDates);
        when(utilService.isReferenceDateEquals(dataReferencia1, transacaoComPagamento.getRecurringReferenceDate())).thenReturn(true);
        when(utilService.isReferenceDateEquals(dataReferencia2, transacaoComPagamento.getRecurringReferenceDate())).thenReturn(false);

        // When
        adjustRecurringCase.execute(recurring, relatedTransactions, saveFunction);

        // Then
        verify(listRecurringReferenceDatesCase).execute(recurring);
        verify(utilService).isReferenceDateEquals(dataReferencia1, transacaoComPagamento.getRecurringReferenceDate());
        verify(utilService).isReferenceDateEquals(dataReferencia2, transacaoComPagamento.getRecurringReferenceDate());
        verify(saveFunction).apply(recurring);
    }

    /**
     * Não deve chamar saveFunction quando status da recorrência não mudou.
     */
    @Test
    void adjustRecurring_naoDeveChamarSaveFunction_quandoStatusRecorrenciaNaoMudou() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring recurring = criarExpenseRecurringAtiva(1L, user);
        
        LocalDateTime dataReferencia1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        List<LocalDateTime> referenceDates = Collections.singletonList(dataReferencia1);
        
        ExpenseTransaction transacaoSemPagamento = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        // Simula transação sem data de pagamento
        try {
            java.lang.reflect.Field paymentDateField = Transaction.class.getDeclaredField("paymentDate");
            paymentDateField.setAccessible(true);
            paymentDateField.set(transacaoSemPagamento, null);
        } catch (Exception e) {
            // Ignora exceção para o teste
        }
        
        List<Transaction> relatedTransactions = Collections.singletonList(transacaoSemPagamento);
        
        @SuppressWarnings("unchecked")
        Function<Recurring, Void> saveFunction = mock(Function.class);
        
        when(listRecurringReferenceDatesCase.execute(recurring)).thenReturn(referenceDates);

        // When
        adjustRecurringCase.execute(recurring, relatedTransactions, saveFunction);

        // Then
        verify(listRecurringReferenceDatesCase).execute(recurring);
        verifyNoInteractions(saveFunction);
    }

    /**
     * Deve processar corretamente quando não há transações relacionadas.
     */
    @Test
    void adjustRecurring_deveProcessarCorretamente_quandoNaoHaTransacoesRelacionadas() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        ExpenseRecurring recurring = criarExpenseRecurringInativa(1L, user);
        
        LocalDateTime dataReferencia = LocalDateTime.of(2024, 1, 1, 0, 0);
        List<LocalDateTime> referenceDates = Collections.singletonList(dataReferencia);
        List<Transaction> relatedTransactions = Collections.emptyList();
        
        @SuppressWarnings("unchecked")
        Function<Recurring, Void> saveFunction = mock(Function.class);
        
        when(listRecurringReferenceDatesCase.execute(recurring)).thenReturn(referenceDates);

        // When
        adjustRecurringCase.execute(recurring, relatedTransactions, saveFunction);

        // Then
        verify(listRecurringReferenceDatesCase).execute(recurring);
        verify(saveFunction).apply(recurring);
    }

    /**
     * Deve processar corretamente quando não há datas de referência.
     */
    @Test
    void adjustRecurring_deveProcessarCorretamente_quandoNaoHaDatasReferencia() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring recurring = criarExpenseRecurringAtiva(1L, user);
        
        List<LocalDateTime> referenceDates = Collections.emptyList();
        ExpenseTransaction transacao = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        List<Transaction> relatedTransactions = Collections.singletonList(transacao);
        
        @SuppressWarnings("unchecked")
        Function<Recurring, Void> saveFunction = mock(Function.class);
        
        when(listRecurringReferenceDatesCase.execute(recurring)).thenReturn(referenceDates);

        // When
        adjustRecurringCase.execute(recurring, relatedTransactions, saveFunction);

        // Then
        verify(listRecurringReferenceDatesCase).execute(recurring);
        verify(saveFunction).apply(recurring);
    }

    /**
     * Deve ativar recorrência quando há uma data de referência sem correspondência em transações pagas.
     */
    @Test
    void adjustRecurring_deveAtivarRecorrencia_quandoHaDataReferenciaSemCorrespondenciaEmTransacoesPagas() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring recurring = criarExpenseRecurringInativa(1L, user);
        
        LocalDateTime dataReferencia1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dataReferencia2 = LocalDateTime.of(2024, 2, 1, 0, 0);
        List<LocalDateTime> referenceDates = Arrays.asList(dataReferencia1, dataReferencia2);
        
        ExpenseTransaction transacaoComPagamento = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        List<Transaction> relatedTransactions = Collections.singletonList(transacaoComPagamento);
        
        @SuppressWarnings("unchecked")
        Function<Recurring, Void> saveFunction = mock(Function.class);
        
        when(listRecurringReferenceDatesCase.execute(recurring)).thenReturn(referenceDates);
        // Primeira data tem correspondência, segunda não tem
        when(utilService.isReferenceDateEquals(dataReferencia1, transacaoComPagamento.getRecurringReferenceDate())).thenReturn(true);
        when(utilService.isReferenceDateEquals(dataReferencia2, transacaoComPagamento.getRecurringReferenceDate())).thenReturn(false);

        // When
        adjustRecurringCase.execute(recurring, relatedTransactions, saveFunction);

        // Then
        verify(listRecurringReferenceDatesCase).execute(recurring);
        verify(utilService).isReferenceDateEquals(dataReferencia1, transacaoComPagamento.getRecurringReferenceDate());
        verify(utilService).isReferenceDateEquals(dataReferencia2, transacaoComPagamento.getRecurringReferenceDate());
        verify(saveFunction).apply(recurring);
    }

    /**
     * Deve processar múltiplas transações corretamente verificando se todas as datas têm correspondência.
     */
    @Test
    void adjustRecurring_deveProcessarMultiplasTransacoes_verificandoSeTodasDatasTerCorrespondencia() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring recurring = criarExpenseRecurringAtiva(1L, user);
        
        LocalDateTime dataReferencia1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime dataReferencia2 = LocalDateTime.of(2024, 2, 1, 0, 0);
        List<LocalDateTime> referenceDates = Arrays.asList(dataReferencia1, dataReferencia2);
        
        ExpenseTransaction transacao1 = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);
        ExpenseTransaction transacao2 = ExpenseTransactionFixture.umaTransacaoComId(2L, user, category);
        List<Transaction> relatedTransactions = Arrays.asList(transacao1, transacao2);
        
        @SuppressWarnings("unchecked")
        Function<Recurring, Void> saveFunction = mock(Function.class);
        
        when(listRecurringReferenceDatesCase.execute(recurring)).thenReturn(referenceDates);
        // Primeira data não tem correspondência - noneMatch deve retornar true
        when(utilService.isReferenceDateEquals(dataReferencia1, transacao1.getRecurringReferenceDate())).thenReturn(false);
        when(utilService.isReferenceDateEquals(dataReferencia1, transacao2.getRecurringReferenceDate())).thenReturn(false);

        // When
        adjustRecurringCase.execute(recurring, relatedTransactions, saveFunction);

        // Then
        verify(listRecurringReferenceDatesCase).execute(recurring);
        // Para dataReferencia1, verifica contra transacao1 e transacao2 (2 chamadas)
        verify(utilService, times(2)).isReferenceDateEquals(eq(dataReferencia1), any());
        verifyNoInteractions(saveFunction); // Status não muda - recurring já é ativo e permanece ativo
    }

    /**
     * Método auxiliar para criar uma ExpenseRecurring ativa.
     */
    private ExpenseRecurring criarExpenseRecurringAtiva(Long id, User user) {
        Category category = CategoryFixture.umaCategoriaComId(id + 1000, CategoryTypeEnum.EXPENSE, user);
        Period period = new Period(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now().plusDays(30));

        ExpenseRecurring expenseRecurring = new ExpenseRecurring(
                "Despesa Recorrente Teste",
                period,
                category,
                new BigDecimal("100.00"),
                user);
        expenseRecurring.setId(id);
        expenseRecurring.setActive(true);
        return expenseRecurring;
    }

    /**
     * Método auxiliar para criar uma ExpenseRecurring inativa.
     */
    private ExpenseRecurring criarExpenseRecurringInativa(Long id, User user) {
        Category category = CategoryFixture.umaCategoriaComId(id + 1000, CategoryTypeEnum.EXPENSE, user);
        Period period = new Period(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now().plusDays(30));

        ExpenseRecurring expenseRecurring = new ExpenseRecurring(
                "Despesa Recorrente Teste",
                period,
                category,
                new BigDecimal("100.00"),
                user);
        expenseRecurring.setId(id);
        expenseRecurring.setActive(false);
        return expenseRecurring;
    }
}
