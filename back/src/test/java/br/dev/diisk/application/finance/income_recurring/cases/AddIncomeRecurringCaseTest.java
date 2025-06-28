package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.finance.income_recurring.dtos.AddIncomeRecurringParams;
import br.dev.diisk.application.finance.income_transaction.cases.AddIncomeTransactionCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

/**
 * Testes unitários para AddIncomeRecurringCase, cobrindo regras herdadas e
 * fluxos de exceção.
 */
@ExtendWith(MockitoExtension.class)
class AddIncomeRecurringCaseTest {

    @Mock
    private IIncomeRecurringRepository incomeRecurringRepository;
    @Mock
    private GetCategoryCase getCategoryCase;
    @Mock
    private AddIncomeTransactionCase addIncomeTransactionCase;
    @Mock
    private UtilService utilService;

    @InjectMocks
    private AddIncomeRecurringCase addIncomeRecurringCase;

    // Fluxo feliz com categoria válida
    @Test
    void addIncomeRecurring_deveSalvarERetornarIncomeRecurring_quandoDadosValidosEComCategoria() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        LocalDateTime startDate = LocalDateTime.now().minusMonths(2); // Início 2 meses atrás
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);
        LocalDateTime firstDayMonth = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), 1, 0, 0);
        LocalDateTime lastDayEndDate = endDate.withDayOfMonth(endDate.getMonth().length(endDate.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
        
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("Salário");
        params.setCategoryId(category.getId());
        params.setValue(BigDecimal.valueOf(1000));
        params.setStartDate(startDate);
        params.setEndDate(endDate);
        
        when(getCategoryCase.execute(user, category.getId())).thenReturn(category);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(lastDayEndDate);
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayMonth);

        // When
        IncomeRecurring result = addIncomeRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(params.getDescription(), result.getDescription());
        assertEquals(category, result.getCategory());
        assertEquals(params.getValue(), result.getValue());
        assertEquals(user, result.getUser());
        assertEquals(startDate, result.getStartDate());
        assertEquals(lastDayEndDate, result.getEndDate());
        verify(incomeRecurringRepository).save(any(IncomeRecurring.class));
        // Deve criar pelo menos uma transação porque startDate é no passado
        verify(addIncomeTransactionCase, atLeastOnce()).execute(eq(user), any(AddIncomeTransactionParams.class));
    }

    // Fluxo feliz sem categoria (categoria nula)
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoCategoriaNula() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("Freelance");
        params.setCategoryId(null);
        params.setValue(BigDecimal.valueOf(500));
        params.setStartDate(LocalDateTime.now().minusDays(1));
        params.setEndDate(LocalDateTime.now().plusDays(10));

        // When/Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> addIncomeRecurringCase.execute(user, params));
        assertEquals("category", ex.getDetails().get("campo"));
    }

    // Categoria de outro usuário
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoCategoriaNaoPertenceAoUsuario() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        User outroUser = UserFixture.umUsuarioComId(2L);
        Category category = CategoryFixture.umaCategoriaComId(3L, CategoryTypeEnum.INCOME, outroUser);
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("Renda Extra");
        params.setCategoryId(category.getId());
        params.setValue(BigDecimal.valueOf(200));
        params.setStartDate(LocalDateTime.now().minusDays(1));
        params.setEndDate(LocalDateTime.now().plusDays(10));
        when(getCategoryCase.execute(user, category.getId())).thenReturn(category);

        // When/Then
        BusinessException ex = assertThrows(BusinessException.class, () -> addIncomeRecurringCase.execute(user, params));
        assertEquals(category.getId().toString(), ex.getDetails().get("categoryId"));
    }

    // Categoria com tipo incompatível
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoCategoriaTipoInvalido() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(4L, CategoryTypeEnum.EXPENSE, user);
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("Renda Inválida");
        params.setCategoryId(category.getId());
        params.setValue(BigDecimal.valueOf(100));
        params.setStartDate(LocalDateTime.now().minusDays(1));
        params.setEndDate(LocalDateTime.now().plusDays(10));
        when(getCategoryCase.execute(user, category.getId())).thenReturn(category);

        // When/Then
        BusinessException ex = assertThrows(BusinessException.class, () -> addIncomeRecurringCase.execute(user, params));
        assertEquals(CategoryTypeEnum.INCOME.name(), ex.getDetails().get("expectedType"));
        assertEquals(CategoryTypeEnum.EXPENSE.name(), ex.getDetails().get("actualType"));
    }

    // Valor nulo ou zero
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoValorNuloOuZero() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(5L, CategoryTypeEnum.INCOME, user);
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("Renda Zero");
        params.setCategoryId(category.getId());
        params.setValue(BigDecimal.ZERO);
        params.setStartDate(LocalDateTime.now().minusDays(1));
        params.setEndDate(LocalDateTime.now().plusDays(10));
        when(getCategoryCase.execute(user, category.getId())).thenReturn(category);

        // When/Then
        BusinessException ex = assertThrows(BusinessException.class, () -> addIncomeRecurringCase.execute(user, params));
        assertEquals("0", ex.getDetails().get("value"));
    }

    // Descrição nula ou vazia
    @Test
    void addIncomeRecurring_deveLancarExcecao_quandoDescricaoNulaOuVazia() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(6L, CategoryTypeEnum.INCOME, user);
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("");
        params.setCategoryId(category.getId());
        params.setValue(BigDecimal.valueOf(100));
        params.setStartDate(LocalDateTime.now().minusDays(1));
        params.setEndDate(LocalDateTime.now().plusDays(10));
        when(getCategoryCase.execute(user, category.getId())).thenReturn(category);

        // When/Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> addIncomeRecurringCase.execute(user, params));
        assertEquals("description", ex.getDetails().get("campo"));
    }

    // Não cria transação se startDate for futura
    @Test
    void addIncomeRecurring_naoDeveCriarTransacaoQuandoStartDateFuturo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(7L, CategoryTypeEnum.INCOME, user);
        LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("Bônus");
        params.setCategoryId(category.getId());
        params.setValue(BigDecimal.valueOf(200));
        params.setStartDate(startDate);
        params.setEndDate(endDate);
        LocalDateTime firstDayMonth = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), 1, 0, 0);
        LocalDateTime lastDayEndDate = endDate.withDayOfMonth(endDate.getMonth().length(endDate.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
        
        when(getCategoryCase.execute(user, category.getId())).thenReturn(category);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(lastDayEndDate);
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayMonth);

        // When
        IncomeRecurring result = addIncomeRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        verify(incomeRecurringRepository).save(any(IncomeRecurring.class));
        verify(addIncomeTransactionCase, never()).execute(any(), any());
    }

    // Não cria transação se endDate passada
    @Test
    void addIncomeRecurring_naoDeveCriarTransacaoQuandoEndDatePassado() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(8L, CategoryTypeEnum.INCOME, user);
        LocalDateTime startDate = LocalDateTime.now().minusDays(10);
        LocalDateTime endDate = LocalDateTime.now().minusDays(2);
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("Investimento");
        params.setCategoryId(category.getId());
        params.setValue(BigDecimal.valueOf(300));
        params.setStartDate(startDate);
        params.setEndDate(endDate);
        LocalDateTime firstDayMonth = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), 1, 0, 0);
        LocalDateTime lastDayEndDate = endDate.withDayOfMonth(endDate.getMonth().length(endDate.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
        
        when(getCategoryCase.execute(user, category.getId())).thenReturn(category);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(lastDayEndDate);
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayMonth);

        // When
        IncomeRecurring result = addIncomeRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        verify(incomeRecurringRepository).save(any(IncomeRecurring.class));
        verify(addIncomeTransactionCase, never()).execute(any(), any());
    }

    // Cria transação normalmente se endDate for nula
    @Test
    void addIncomeRecurring_deveCriarTransacaoQuandoEndDateNula() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(9L, CategoryTypeEnum.INCOME, user);
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1); // Início 1 mês atrás
        AddIncomeRecurringParams params = new AddIncomeRecurringParams();
        params.setDescription("Renda vitalícia");
        params.setCategoryId(category.getId());
        params.setValue(BigDecimal.valueOf(400));
        params.setStartDate(startDate);
        params.setEndDate(null);
        LocalDateTime firstDayMonth = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), 1, 0, 0);
        
        when(getCategoryCase.execute(user, category.getId())).thenReturn(category);
        when(utilService.getFirstDayMonthReference(startDate)).thenReturn(firstDayMonth);

        // When
        IncomeRecurring result = addIncomeRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        verify(incomeRecurringRepository).save(any(IncomeRecurring.class));
        // Deve criar pelo menos uma transação porque startDate é no passado e endDate é null
        verify(addIncomeTransactionCase, atLeastOnce()).execute(eq(user), any(AddIncomeTransactionParams.class));
    }
}
