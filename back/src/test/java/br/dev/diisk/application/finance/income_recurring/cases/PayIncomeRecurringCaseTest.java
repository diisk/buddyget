package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.application.finance.income_recurring.dtos.PayIncomeRecurringParams;
import br.dev.diisk.application.finance.income_transaction.cases.AddIncomeTransactionCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransactionFixture;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.goal.GoalFixture;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para PayIncomeRecurringCase.
 */
@ExtendWith(MockitoExtension.class)
class PayIncomeRecurringCaseTest {

    @Mock
    private GetIncomeRecurringCase getIncomeRecurringCase;

    @Mock
    private AddIncomeTransactionCase addIncomeTransactionCase;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private PayIncomeRecurringCase payIncomeRecurringCase;

    @Test
    @DisplayName("Deve pagar receita recorrente corretamente quando todos os parâmetros são válidos")
    void payIncomeRecurring_devePagarReceitaRecorrenteCorretamente_quandoTodosParametrosSaoValidos() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 1L;
        Long incomeRecurringId = 10L;
        LocalDateTime paymentDate = LocalDateTime.now();
        LocalDateTime referenceDate = LocalDateTime.now().withDayOfMonth(15);
        LocalDateTime firstDayOfMonth = referenceDate.withDayOfMonth(1);

        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(incomeRecurringId, user, category, recurringDay, period);

        PayIncomeRecurringParams params = new PayIncomeRecurringParams(paymentDate, referenceDate);
        IncomeTransaction expectedTransaction = IncomeTransactionFixture.umaIncomeTransactionComId(1L);

        when(getIncomeRecurringCase.execute(user, incomeRecurringId)).thenReturn(incomeRecurring);
        when(utilService.getFirstDayMonthReference(referenceDate)).thenReturn(firstDayOfMonth);
        when(addIncomeTransactionCase.execute(eq(user), any(AddIncomeTransactionParams.class))).thenReturn(expectedTransaction);

        // When - Executa o método a ser testado
        IncomeTransaction result = payIncomeRecurringCase.execute(user, incomeRecurringId, params);

        // Then - Verifica os resultados
        assertNotNull(result);
        assertEquals(expectedTransaction, result);

        verify(getIncomeRecurringCase).execute(user, incomeRecurringId);
        verify(utilService).getFirstDayMonthReference(referenceDate);

        ArgumentCaptor<AddIncomeTransactionParams> paramsCaptor = ArgumentCaptor.forClass(AddIncomeTransactionParams.class);
        verify(addIncomeTransactionCase).execute(eq(user), paramsCaptor.capture());

        AddIncomeTransactionParams capturedParams = paramsCaptor.getValue();
        assertEquals(incomeRecurring.getCategoryId(), capturedParams.getCategoryId());
        assertEquals(incomeRecurring.getDescription(), capturedParams.getDescription());
        assertEquals(incomeRecurring.getValue(), capturedParams.getValue());
        assertNull(capturedParams.getGoalId());
        assertEquals(firstDayOfMonth, capturedParams.getRecurringReferenceDate());
        assertEquals(incomeRecurring.getId(), capturedParams.getIncomeRecurringId());
    }

    @Test
    @DisplayName("Deve pagar receita recorrente com goal quando receita possui goal associado")
    void payIncomeRecurring_devePagarReceitaRecorrenteComGoal_quandoReceitaPossuiGoalAssociado() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 1L;
        Long incomeRecurringId = 10L;
        Long goalId = 5L;
        LocalDateTime paymentDate = LocalDateTime.now();
        LocalDateTime referenceDate = LocalDateTime.now().withDayOfMonth(15);
        LocalDateTime firstDayOfMonth = referenceDate.withDayOfMonth(1);

        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Goal goal = GoalFixture.umGoalComId(goalId, user);
        Period period = new Period(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(incomeRecurringId, user, category, recurringDay, period);
        incomeRecurring.addGoal(goal);

        PayIncomeRecurringParams params = new PayIncomeRecurringParams(paymentDate, referenceDate);
        IncomeTransaction expectedTransaction = IncomeTransactionFixture.umaIncomeTransactionComId(1L);

        when(getIncomeRecurringCase.execute(user, incomeRecurringId)).thenReturn(incomeRecurring);
        when(utilService.getFirstDayMonthReference(referenceDate)).thenReturn(firstDayOfMonth);
        when(addIncomeTransactionCase.execute(eq(user), any(AddIncomeTransactionParams.class))).thenReturn(expectedTransaction);

        // When - Executa o método a ser testado
        IncomeTransaction result = payIncomeRecurringCase.execute(user, incomeRecurringId, params);

        // Then - Verifica os resultados
        assertNotNull(result);
        assertEquals(expectedTransaction, result);

        ArgumentCaptor<AddIncomeTransactionParams> paramsCaptor = ArgumentCaptor.forClass(AddIncomeTransactionParams.class);
        verify(addIncomeTransactionCase).execute(eq(user), paramsCaptor.capture());

        AddIncomeTransactionParams capturedParams = paramsCaptor.getValue();
        assertEquals(goalId, capturedParams.getGoalId());
    }

    @Test
    @DisplayName("Deve lançar NullOrEmptyException quando data de pagamento é nula")
    void payIncomeRecurring_deveLancarNullOrEmptyException_quandoDataPagamentoNula() {
        // Given - Prepara os dados de entrada
        Long userId = 1L;
        Long incomeRecurringId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        PayIncomeRecurringParams params = new PayIncomeRecurringParams(null, LocalDateTime.now());

        // When & Then - Executa o método e verifica a exceção
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () ->
                payIncomeRecurringCase.execute(user, incomeRecurringId, params)
        );

        assertEquals("A data de pagamento é obrigatória", ex.getMessage());
        assertTrue(ex.getDetails().isEmpty()); // Verifica que os details estão vazios
        verifyNoInteractions(getIncomeRecurringCase, addIncomeTransactionCase, utilService);
    }

    @Test
    @DisplayName("Deve lançar NullOrEmptyException quando data de referência é nula")
    void payIncomeRecurring_deveLancarNullOrEmptyException_quandoDataReferenciaNula() {
        // Given - Prepara os dados de entrada
        Long userId = 1L;
        Long incomeRecurringId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        PayIncomeRecurringParams params = new PayIncomeRecurringParams(LocalDateTime.now(), null);

        // When & Then - Executa o método e verifica a exceção
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () ->
                payIncomeRecurringCase.execute(user, incomeRecurringId, params)
        );

        assertEquals("A data de referência é obrigatória", ex.getMessage());
        assertTrue(ex.getDetails().isEmpty()); // Verifica que os details estão vazios
        verifyNoInteractions(getIncomeRecurringCase, addIncomeTransactionCase, utilService);
    }

    @Test
    @DisplayName("Deve lançar NullOrEmptyException quando id da receita recorrente é nulo")
    void payIncomeRecurring_deveLancarNullOrEmptyException_quandoIdReceitaRecorrenteNulo() {
        // Given - Prepara os dados de entrada
        Long userId = 1L;
        User user = UserFixture.umUsuarioComId(userId);
        PayIncomeRecurringParams params = new PayIncomeRecurringParams(LocalDateTime.now(), LocalDateTime.now());

        // When & Then - Executa o método e verifica a exceção
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () ->
                payIncomeRecurringCase.execute(user, null, params)
        );

        assertEquals("O id da receita recorrente é obrigatório", ex.getMessage());
        assertTrue(ex.getDetails().isEmpty()); // Verifica que os details estão vazios
        verifyNoInteractions(getIncomeRecurringCase, addIncomeTransactionCase, utilService);
    }
}
