package br.dev.diisk.application.finance.income_transaction.cases;

import br.dev.diisk.application.finance.income_recurring.cases.ListCurrentIncomeRecurringsCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para GenerateMonthlyIncomeTransactionCase.
 * Testa todos os cenários relevantes seguindo o padrão Given-When-Then.
 */
@ExtendWith(MockitoExtension.class)
class GenerateMonthlyIncomeTransactionCaseTest {

    @Mock
    private ListCurrentIncomeRecurringsCase listCurrentIncomeRecurringsCase;

    @Mock
    private IIncomeTransactionRepository incomeTransactionRepository;

    @Mock
    private AddIncomeTransactionCase addIncomeTransactionCase;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private GenerateMonthlyIncomeTransactionCase generateMonthlyIncomeTransactionCase;

    @Test
    void generateMonthlyIncomeTransaction_deveGerarTransacaoCorretamente_quandoExistemRecorrentesAtivasETransacaoNaoExiste() {
        // Given: existem receitas recorrentes ativas e nenhuma transação foi criada no mês atual
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);
        DataRange period = new DataRange(startOfMonth, startOfMonth.plusMonths(6));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(1L, user, category,recurringDay , period);
        Set<IncomeRecurring> recurrings = Set.of(incomeRecurring);

        when(listCurrentIncomeRecurringsCase.execute()).thenReturn(recurrings);
        when(utilService.getFirstDayMonthReference(any(LocalDateTime.class))).thenReturn(startOfMonth);
        when(incomeTransactionRepository.existsByRecurring(eq(1L), eq(startOfMonth), eq(startOfNextMonth)))
                .thenReturn(false);

        // When: executa o caso de uso
        generateMonthlyIncomeTransactionCase.execute();

        // Then: verifica se o método addIncomeTransactionCase foi chamado com os parâmetros corretos
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<AddIncomeTransactionParams> paramsCaptor = ArgumentCaptor.forClass(AddIncomeTransactionParams.class);
        verify(addIncomeTransactionCase).execute(userCaptor.capture(), paramsCaptor.capture());

        User capturedUser = userCaptor.getValue();
        AddIncomeTransactionParams capturedParams = paramsCaptor.getValue();

        assertEquals(user, capturedUser);
        assertEquals(incomeRecurring.getCategoryId(), capturedParams.getCategoryId());
        assertEquals(incomeRecurring.getDescription(), capturedParams.getDescription());
        assertEquals(incomeRecurring.getValue(), capturedParams.getValue());
        assertEquals(incomeRecurring.getId(), capturedParams.getIncomeRecurringId());
        
        // Verifica se o UtilService foi chamado corretamente
        verify(utilService).getFirstDayMonthReference(any(LocalDateTime.class));
    }

    @Test
    void generateMonthlyIncomeTransaction_naoDeveGerarTransacao_quandoJaExisteTransacaoNoMesAtual() {
        // Given: existe uma receita recorrente ativa mas já foi criada uma transação no mês atual
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);
        DataRange period = new DataRange(startOfMonth, startOfMonth.plusMonths(6));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(1L, user, category,recurringDay , period);
        Set<IncomeRecurring> recurrings = Set.of(incomeRecurring);

        when(listCurrentIncomeRecurringsCase.execute()).thenReturn(recurrings);
        when(utilService.getFirstDayMonthReference(any(LocalDateTime.class))).thenReturn(startOfMonth);
        when(incomeTransactionRepository.existsByRecurring(eq(1L), eq(startOfMonth), eq(startOfNextMonth)))
                .thenReturn(true);

        // When: executa o caso de uso
        generateMonthlyIncomeTransactionCase.execute();

        // Then: verifica que nenhuma transação foi criada
        verify(addIncomeTransactionCase, never()).execute(any(User.class), any(AddIncomeTransactionParams.class));
        verify(utilService).getFirstDayMonthReference(any(LocalDateTime.class));
    }

    @Test
    void generateMonthlyIncomeTransaction_naoDeveGerarTransacao_quandoNaoExistemRecorrentesAtivas() {
        // Given: não existem receitas recorrentes ativas
        Set<IncomeRecurring> recurrings = Collections.emptySet();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);

        when(listCurrentIncomeRecurringsCase.execute()).thenReturn(recurrings);
        when(utilService.getFirstDayMonthReference(any(LocalDateTime.class))).thenReturn(startOfMonth);

        // When: executa o caso de uso
        generateMonthlyIncomeTransactionCase.execute();

        // Then: verifica que nenhuma transação foi criada e o repository não foi consultado
        verify(incomeTransactionRepository, never()).existsByRecurring(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(addIncomeTransactionCase, never()).execute(any(User.class), any(AddIncomeTransactionParams.class));
        // O utilService é sempre chamado para calcular as datas, mesmo sem recorrentes
        verify(utilService).getFirstDayMonthReference(any(LocalDateTime.class));
    }

    @Test
    void generateMonthlyIncomeTransaction_deveGerarMultiplasTransacoes_quandoExistemMultiplasRecorrentesAtivasSemTransacaoNoMes() {
        // Given: existem múltiplas receitas recorrentes ativas sem transações no mês atual
        User user1 = UserFixture.umUsuarioComId(1L);
        User user2 = UserFixture.umUsuarioComId(2L);
        Category category1 = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user1);
        Category category2 = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user2);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);
        DataRange period = new DataRange(startOfMonth, startOfMonth.plusMonths(6));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring1 = IncomeRecurringFixture.umIncomeRecurringComId(1L, user1, category1,recurringDay , period);
        IncomeRecurring incomeRecurring2 = IncomeRecurringFixture.umIncomeRecurringComId(2L, user2, category2,recurringDay , period);
        Set<IncomeRecurring> recurrings = Set.of(incomeRecurring1, incomeRecurring2);

        when(listCurrentIncomeRecurringsCase.execute()).thenReturn(recurrings);
        when(utilService.getFirstDayMonthReference(any(LocalDateTime.class))).thenReturn(startOfMonth);
        when(incomeTransactionRepository.existsByRecurring(anyLong(), eq(startOfMonth), eq(startOfNextMonth)))
                .thenReturn(false);

        // When: executa o caso de uso
        generateMonthlyIncomeTransactionCase.execute();

        // Then: verifica que duas transações foram criadas
        verify(addIncomeTransactionCase, times(2)).execute(any(User.class), any(AddIncomeTransactionParams.class));
        verify(incomeTransactionRepository, times(2)).existsByRecurring(anyLong(), eq(startOfMonth), eq(startOfNextMonth));
        verify(utilService, times(1)).getFirstDayMonthReference(any(LocalDateTime.class));
    }

    @Test
    void generateMonthlyIncomeTransaction_deveGerarApenasParcialTransacoes_quandoAlgumasJaExistemNoMes() {
        // Given: múltiplas receitas recorrentes, sendo uma já possui transação no mês e outra não
        User user1 = UserFixture.umUsuarioComId(1L);
        User user2 = UserFixture.umUsuarioComId(2L);
        Category category1 = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user1);
        Category category2 = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user2);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);
        DataRange period = new DataRange(startOfMonth, startOfMonth.plusMonths(6));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring1 = IncomeRecurringFixture.umIncomeRecurringComId(1L, user1, category1,recurringDay , period);
        IncomeRecurring incomeRecurring2 = IncomeRecurringFixture.umIncomeRecurringComId(2L, user2, category2,recurringDay , period);
        Set<IncomeRecurring> recurrings = Set.of(incomeRecurring1, incomeRecurring2);

        when(listCurrentIncomeRecurringsCase.execute()).thenReturn(recurrings);
        when(utilService.getFirstDayMonthReference(any(LocalDateTime.class))).thenReturn(startOfMonth);
        when(incomeTransactionRepository.existsByRecurring(eq(1L), eq(startOfMonth), eq(startOfNextMonth)))
                .thenReturn(true); // primeira já tem transação
        when(incomeTransactionRepository.existsByRecurring(eq(2L), eq(startOfMonth), eq(startOfNextMonth)))
                .thenReturn(false); // segunda não tem transação

        // When: executa o caso de uso
        generateMonthlyIncomeTransactionCase.execute();

        // Then: verifica que apenas uma transação foi criada (a que não existia)
        verify(addIncomeTransactionCase, times(1)).execute(any(User.class), any(AddIncomeTransactionParams.class));
        verify(incomeTransactionRepository, times(2)).existsByRecurring(anyLong(), eq(startOfMonth), eq(startOfNextMonth));
        verify(utilService, times(1)).getFirstDayMonthReference(any(LocalDateTime.class));
    }

    @Test
    void generateMonthlyIncomeTransaction_deveUsarDataCorreta_quandoCalculaPeriodoDoMes() {
        // Given: uma receita recorrente ativa
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);
        DataRange period = new DataRange(startOfMonth, startOfMonth.plusMonths(6));
        DayOfMonth recurringDay = new DayOfMonth(1);
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(1L, user, category,recurringDay , period);
        Set<IncomeRecurring> recurrings = Set.of(incomeRecurring);

        when(listCurrentIncomeRecurringsCase.execute()).thenReturn(recurrings);
        when(utilService.getFirstDayMonthReference(any(LocalDateTime.class))).thenReturn(startOfMonth);
        when(incomeTransactionRepository.existsByRecurring(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);

        // When: executa o caso de uso
        generateMonthlyIncomeTransactionCase.execute();

        // Then: verifica se as datas passadas para o repository estão corretas
        verify(incomeTransactionRepository).existsByRecurring(
                eq(1L), 
                eq(startOfMonth), 
                eq(startOfNextMonth)
        );
        
        // Verifica se o UtilService foi chamado para calcular o primeiro dia do mês
        verify(utilService).getFirstDayMonthReference(any(LocalDateTime.class));
    }
}
