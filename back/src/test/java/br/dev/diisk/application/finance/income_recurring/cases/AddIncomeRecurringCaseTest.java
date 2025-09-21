package br.dev.diisk.application.finance.income_recurring.cases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.finance.income_recurring.dtos.AddIncomeRecurringParams;
import br.dev.diisk.application.goal.cases.GetGoalCase;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.goal.GoalFixture;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

/**
 * Testes unitários para o caso de uso AddIncomeRecurringCase.
 * Segue o padrão Given-When-Then e cobre cenários felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class AddIncomeRecurringCaseTest {

    @Mock
    private IIncomeRecurringRepository incomeRecurringRepository;
    
    @Mock
    private GetCategoryCase getCategoryCase;
    
    @Mock
    private UtilService utilService;
    
    @Mock
    private GetGoalCase getGoalCase;
    
    @InjectMocks
    private AddIncomeRecurringCase addIncomeRecurringCase;

    /**
     * Deve criar uma receita recorrente corretamente quando todos os dados são válidos com categoria e meta.
     */
    @Test
    void addIncomeRecurring_deveCriarReceitaRecorrente_quandoDadosValidosComCategoriaEMeta() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        Goal goal = GoalFixture.umGoalComId(3L, user);
        
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 15, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 20, 15, 0);
        LocalDateTime endDateProcessed = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Salário mensal",
                new BigDecimal("5000.00"),
                2L,
                3L,
                startDate,
                endDate
        );
        
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        when(getGoalCase.execute(user, 3L)).thenReturn(goal);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(endDateProcessed);
        when(incomeRecurringRepository.save(any(IncomeRecurring.class))).thenAnswer(invocation -> {
            IncomeRecurring saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });
        
        // When
        IncomeRecurring result = addIncomeRecurringCase.execute(user, params);
        
        // Then
        assertNotNull(result);
        assertEquals("Salário mensal", result.getDescription());
        assertEquals(new BigDecimal("5000.00"), result.getValue());
        assertEquals(category, result.getCategory());
        assertEquals(goal, result.getGoal());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDateProcessed, result.getEndDate());
        assertEquals(user.getId(), result.getUserId());
        
        verify(getCategoryCase).execute(user, 2L);
        verify(getGoalCase).execute(user, 3L);
        verify(utilService).getLastDayMonthReference(endDate);
        verify(incomeRecurringRepository).save(any(IncomeRecurring.class));
    }

    /**
     * Deve criar uma receita recorrente corretamente quando apenas categoria é informada.
     */
    @Test
    void addIncomeRecurring_deveCriarReceitaRecorrente_quandoApenasCategoriaMnformada() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 15, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 20, 15, 0);
        LocalDateTime endDateProcessed = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Receita extra",
                new BigDecimal("1000.00"),
                2L,
                null,
                startDate,
                endDate
        );
        
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(endDateProcessed);
        when(incomeRecurringRepository.save(any(IncomeRecurring.class))).thenAnswer(invocation -> {
            IncomeRecurring saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });
        
        // When
        IncomeRecurring result = addIncomeRecurringCase.execute(user, params);
        
        // Then
        assertNotNull(result);
        assertEquals("Receita extra", result.getDescription());
        assertEquals(new BigDecimal("1000.00"), result.getValue());
        assertEquals(category, result.getCategory());
        assertNull(result.getGoal());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDateProcessed, result.getEndDate());
        assertEquals(user.getId(), result.getUserId());
        
        verify(getCategoryCase).execute(user, 2L);
        verify(getGoalCase, never()).execute(any(), any());
        verify(utilService).getLastDayMonthReference(endDate);
        verify(incomeRecurringRepository).save(any(IncomeRecurring.class));
    }

    /**
     * Deve criar uma receita recorrente corretamente quando data de fim é nula.
     */
    @Test
    void addIncomeRecurring_deveCriarReceitaRecorrente_quandoDataFimNula() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 15, 10, 0);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Receita indefinida",
                new BigDecimal("2000.00"),
                2L,
                null,
                startDate,
                null
        );
        
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        when(incomeRecurringRepository.save(any(IncomeRecurring.class))).thenAnswer(invocation -> {
            IncomeRecurring saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });
        
        // When
        IncomeRecurring result = addIncomeRecurringCase.execute(user, params);
        
        // Then
        assertNotNull(result);
        assertEquals("Receita indefinida", result.getDescription());
        assertEquals(new BigDecimal("2000.00"), result.getValue());
        assertEquals(category, result.getCategory());
        assertNull(result.getGoal());
        assertEquals(startDate, result.getStartDate());
        assertNull(result.getEndDate());
        assertEquals(user.getId(), result.getUserId());
        
        verify(getCategoryCase).execute(user, 2L);
        verify(getGoalCase, never()).execute(any(), any());
        verify(utilService, never()).getLastDayMonthReference(any());
        verify(incomeRecurringRepository).save(any(IncomeRecurring.class));
    }

    /**
     * Deve propagar exceção quando getCategoryCase falha ao buscar categoria.
     */
    @Test
    void addIncomeRecurring_devePropagaExcecao_quandoGetCategoryCaseFalha() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Receita teste",
                new BigDecimal("1000.00"),
                999L,
                null,
                LocalDateTime.of(2025, 1, 15, 10, 0),
                null
        );
        
        RuntimeException expectedException = new RuntimeException("Categoria não encontrada");
        when(getCategoryCase.execute(user, 999L)).thenThrow(expectedException);
        
        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                addIncomeRecurringCase.execute(user, params)
        );
        
        assertEquals("Categoria não encontrada", ex.getMessage());
        verify(getCategoryCase).execute(user, 999L);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    /**
     * Deve propagar exceção quando getGoalCase falha ao buscar meta.
     */
    @Test
    void addIncomeRecurring_devePropagaExcecao_quandoGetGoalCaseFalha() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Receita teste",
                new BigDecimal("1000.00"),
                2L,
                999L,
                LocalDateTime.of(2025, 1, 15, 10, 0),
                null
        );
        
        RuntimeException expectedException = new RuntimeException("Meta não encontrada");
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        when(getGoalCase.execute(user, 999L)).thenThrow(expectedException);
        
        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                addIncomeRecurringCase.execute(user, params)
        );
        
        assertEquals("Meta não encontrada", ex.getMessage());
        verify(getCategoryCase).execute(user, 2L);
        verify(getGoalCase).execute(user, 999L);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    /**
     * Deve lançar exceção quando parâmetros têm descrição nula.
     */
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoDescricaoNula() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                null,
                new BigDecimal("1000.00"),
                2L,
                null,
                LocalDateTime.of(2025, 1, 15, 10, 0),
                null
        );
        
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        
        // When / Then
        assertThrows(Exception.class, () ->
                addIncomeRecurringCase.execute(user, params)
        );
        
        verify(getCategoryCase).execute(user, 2L);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    /**
     * Deve lançar exceção quando parâmetros têm valor nulo.
     */
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoValorNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Receita teste",
                null,
                2L,
                null,
                LocalDateTime.of(2025, 1, 15, 10, 0),
                null
        );
        
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        
        // When / Then
        assertThrows(Exception.class, () ->
                addIncomeRecurringCase.execute(user, params)
        );
        
        verify(getCategoryCase).execute(user, 2L);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    /**
     * Deve lançar exceção quando parâmetros têm valor zero.
     */
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoValorZero() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Receita teste",
                BigDecimal.ZERO,
                2L,
                null,
                LocalDateTime.of(2025, 1, 15, 10, 0),
                null
        );
        
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        
        // When / Then
        assertThrows(Exception.class, () ->
                addIncomeRecurringCase.execute(user, params)
        );
        
        verify(getCategoryCase).execute(user, 2L);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    /**
     * Deve lançar exceção quando parâmetros têm valor negativo.
     */
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoValorNegativo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Receita teste",
                new BigDecimal("-100.00"),
                2L,
                null,
                LocalDateTime.of(2025, 1, 15, 10, 0),
                null
        );
        
        when(getCategoryCase.execute(user, 2L)).thenReturn(category);
        
        // When / Then
        assertThrows(Exception.class, () ->
                addIncomeRecurringCase.execute(user, params)
        );
        
        verify(getCategoryCase).execute(user, 2L);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    /**
     * Deve lançar exceção quando categoria é do tipo incorreto (EXPENSE ao invés de INCOME).
     */
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoCategoriaComTipoIncorreto() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category categoryExpense = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.EXPENSE, user);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams(
                "Receita teste",
                new BigDecimal("1000.00"),
                2L,
                null,
                LocalDateTime.of(2025, 1, 15, 10, 0),
                null
        );
        
        when(getCategoryCase.execute(user, 2L)).thenReturn(categoryExpense);
        
        // When / Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                addIncomeRecurringCase.execute(user, params)
        );
        
        assertEquals("INCOME", ex.getDetails().get("expectedType"));
        assertEquals("EXPENSE", ex.getDetails().get("actualType"));
        
        verify(getCategoryCase).execute(user, 2L);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }
}