package br.dev.diisk.application.finance.income_transaction.cases;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.finance.income_recurring.cases.GetIncomeRecurringCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.goal.cases.GetGoalCase;
import br.dev.diisk.application.monthly_summary.cases.AddMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.goal.GoalFixture;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.UserFixture;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
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
 * Testes unitários para AddIncomeTransactionCase.
 */
@ExtendWith(MockitoExtension.class)
class AddIncomeTransactionCaseTest {

    @Mock
    private IIncomeTransactionRepository incomeRepository;
    @Mock
    private GetCategoryCase getCategoryCase;
    @Mock
    private GetIncomeRecurringCase getIncomeRecurringCase;
    @Mock
    private GetGoalCase getGoalCase;
    @Mock
    private AddMonthlySummaryValueCase addMonthlySummaryValueCase;

    @InjectMocks
    private AddIncomeTransactionCase addIncomeTransactionCase;

    // Teste para o caminho feliz: todos os campos preenchidos
    @Test
    void addIncomeTransaction_deveSalvarTransacaoComTodosOsCampos_quandoTodosOsDadosForemInformados() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        Goal goal = GoalFixture.umGoalComId(3L, user);
        // DataRange válido: endDate deve ser null para não lançar exceção
        DataRange period = new DataRange(LocalDateTime.now().minusMonths(1), null);
        DayOfMonth recurringDay = new DayOfMonth(15);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(4L, user, category,
                recurringDay, period);
        BigDecimal value = new BigDecimal("100.00");
        LocalDateTime date = LocalDateTime.of(2025, 6, 22, 10, 0);
        LocalDateTime recurringDate = LocalDateTime.of(2025, 6, 15, 12, 0);
        AddIncomeTransactionParams params = mock(AddIncomeTransactionParams.class);
        when(params.getDescription()).thenReturn("Salário");
        when(params.getCategoryId()).thenReturn(2L);
        when(params.getGoalId()).thenReturn(3L);
        when(params.getIncomeRecurringId()).thenReturn(4L);
        when(params.getRecurringReferenceDate()).thenReturn(recurringDate);
        when(params.getValue()).thenReturn(value);
        when(params.getReceiptDate()).thenReturn(date);
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        when(getGoalCase.execute(user, 3L)).thenReturn(goal);
        when(getIncomeRecurringCase.execute(user, 4L)).thenReturn(incomeRecurring);
        // When
        IncomeTransaction result = addIncomeTransactionCase.execute(user, params);
        // Then
        ArgumentCaptor<IncomeTransaction> captor = ArgumentCaptor.forClass(IncomeTransaction.class);
        verify(incomeRepository).save(captor.capture());
        IncomeTransaction saved = captor.getValue();
        assertEquals("Salário", saved.getDescription());
        assertEquals(category, saved.getCategory());
        assertEquals(value, saved.getValue());
        assertEquals(date, saved.getReceiptDate());
        assertEquals(user, saved.getUser());
        assertEquals(goal, saved.getGoal());
        assertEquals(incomeRecurring, saved.getIncomeRecurring());
        verify(addMonthlySummaryValueCase).execute(eq(user), any(AddMonthlySummaryValueParams.class));
        assertEquals(saved, result);
    }

    @Test
    void addIncomeTransaction_deveSalvarTransacaoSemGoalOuRecurring_quandoIdsForemNulos() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        BigDecimal value = new BigDecimal("50.00");
        LocalDateTime date = LocalDateTime.of(2025, 6, 22, 10, 0);
        AddIncomeTransactionParams params = mock(AddIncomeTransactionParams.class);
        when(params.getDescription()).thenReturn("Bônus");
        when(params.getCategoryId()).thenReturn(2L);
        when(params.getGoalId()).thenReturn(null);
        when(params.getIncomeRecurringId()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getValue()).thenReturn(value);
        when(params.getReceiptDate()).thenReturn(date);
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        // When
        IncomeTransaction result = addIncomeTransactionCase.execute(user, params);
        // Then
        ArgumentCaptor<IncomeTransaction> captor = ArgumentCaptor.forClass(IncomeTransaction.class);
        verify(incomeRepository).save(captor.capture());
        IncomeTransaction saved = captor.getValue();
        assertEquals("Bônus", saved.getDescription());
        assertEquals(category, saved.getCategory());
        assertEquals(value, saved.getValue());
        assertEquals(date, saved.getReceiptDate());
        assertEquals(user, saved.getUser());
        assertNull(saved.getGoal());
        assertNull(saved.getIncomeRecurring());
        verify(addMonthlySummaryValueCase).execute(eq(user), any(AddMonthlySummaryValueParams.class));
        assertEquals(saved, result);
    }

    @Test
    void addIncomeTransaction_naoDeveAtualizarResumoMensal_quandoDataForNula() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        BigDecimal value = new BigDecimal("200.00");
        AddIncomeTransactionParams params = mock(AddIncomeTransactionParams.class);
        when(params.getDescription()).thenReturn("Prêmio");
        when(params.getCategoryId()).thenReturn(2L);
        when(params.getGoalId()).thenReturn(null);
        when(params.getIncomeRecurringId()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getValue()).thenReturn(value);
        when(params.getReceiptDate()).thenReturn(null);
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        // When
        IncomeTransaction result = addIncomeTransactionCase.execute(user, params);
        // Then
        verify(addMonthlySummaryValueCase, never()).execute(any(), any());
        assertEquals("Prêmio", result.getDescription());
        assertEquals(category, result.getCategory());
        assertEquals(value, result.getValue());
        assertNull(result.getReceiptDate());
        assertEquals(user, result.getUser());
    }

    @Test
    void addIncomeTransaction_deveLancarExcecao_quandoCategoriaForNula() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        BigDecimal value = new BigDecimal("50.00");
        LocalDateTime date = LocalDateTime.of(2025, 6, 22, 10, 0);
        AddIncomeTransactionParams params = mock(AddIncomeTransactionParams.class);
        when(params.getDescription()).thenReturn("Bônus");
        when(params.getCategoryId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getIncomeRecurringId()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getValue()).thenReturn(value);
        when(params.getReceiptDate()).thenReturn(date);

        // When / Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class,
                () -> addIncomeTransactionCase.execute(user, params));
        // Valida a chave do details
        assertEquals("category", ex.getDetails().get("campo"));
    }

    @Test
    void addIncomeTransaction_deveLancarExcecao_quandoValorForNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        AddIncomeTransactionParams params = mock(AddIncomeTransactionParams.class);
        when(params.getDescription()).thenReturn("Outro");
        when(params.getCategoryId()).thenReturn(2L);
        when(params.getGoalId()).thenReturn(null);
        when(params.getIncomeRecurringId()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getValue()).thenReturn(null);
        when(params.getReceiptDate()).thenReturn(null);
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);

        // When / Then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> addIncomeTransactionCase.execute(user, params));
        assertEquals("null", ex.getDetails().get("value"));
    }

    @Test
    void addIncomeTransaction_deveLancarExcecao_quandoValorForZeroOuNegativo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        AddIncomeTransactionParams params = mock(AddIncomeTransactionParams.class);
        when(params.getDescription()).thenReturn("Outro");
        when(params.getCategoryId()).thenReturn(2L);
        when(params.getGoalId()).thenReturn(null);
        when(params.getIncomeRecurringId()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getValue()).thenReturn(BigDecimal.ZERO);
        when(params.getReceiptDate()).thenReturn(null);
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);

        // When / Then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> addIncomeTransactionCase.execute(user, params));
        assertEquals("0", ex.getDetails().get("value"));
    }

    @Test
    void addIncomeTransaction_deveLancarExcecao_quandoIncomeRecurringForInformadoMasRecurringDateForNula() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        DataRange period = new DataRange(LocalDateTime.now().minusMonths(1), null);
        DayOfMonth recurringDay = new DayOfMonth(15);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(4L, user, category,
                recurringDay, period);
        BigDecimal value = new BigDecimal("100.00");
        LocalDateTime date = LocalDateTime.of(2025, 6, 22, 10, 0);
        AddIncomeTransactionParams params = mock(AddIncomeTransactionParams.class);
        when(params.getDescription()).thenReturn("Salário");
        when(params.getCategoryId()).thenReturn(2L);
        when(params.getGoalId()).thenReturn(null);
        when(params.getIncomeRecurringId()).thenReturn(4L);
        when(params.getRecurringReferenceDate()).thenReturn(null); // Null quando incomeRecurringId é informado
        when(params.getValue()).thenReturn(value);
        when(params.getReceiptDate()).thenReturn(date);
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        when(getIncomeRecurringCase.execute(user, 4L)).thenReturn(incomeRecurring);

        // When / Then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> addIncomeTransactionCase.execute(user, params));
        assertTrue(ex.getMessage().contains("A data da referência da recorrencia deve ser informada se a receita for recorrente"));
    }
}
