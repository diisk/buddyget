package br.dev.diisk.application.finance.expense_recurring.cases;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.credit_card.cases.GetCreditCardCase;
import br.dev.diisk.application.finance.expense_recurring.dtos.AddExpenseRecurringParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.application.wish_list.cases.GetWishListItemCase;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.CreditCardFixture;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import br.dev.diisk.domain.wish_list.WishListItem;
import br.dev.diisk.domain.wish_list.WishListItemFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso AddExpenseRecurringCase.
 */
@ExtendWith(MockitoExtension.class)
class AddExpenseRecurringCaseTest {

    @Mock
    private IExpenseRecurringRepository expenseRecurringRepository;

    @Mock
    private GetCategoryCase getCategoryCase;

    @Mock
    private GetWishListItemCase getWishListItemCase;

    @Mock
    private GetCreditCardCase getCreditCardCase;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private AddExpenseRecurringCase addExpenseRecurringCase;

    // Teste para o caminho feliz: deve criar uma despesa recorrente básica com dados mínimos obrigatórios
    @Test
    void addExpenseRecurring_deveCriarDespesaRecorrenteBasica_quandoApenasParametrosObrigatoriosPreenchidos() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        String description = "Despesa Teste";
        BigDecimal value = new BigDecimal("100.00");
        LocalDateTime startDate = LocalDateTime.now();
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription(description);
        params.setValue(value);
        params.setStartDate(startDate);
        params.setCategoryId(categoryId);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(description, result.getDescription());
        assertEquals(value, result.getValue());
        assertEquals(startDate, result.getStartDate());
        assertNull(result.getEndDate());
        assertEquals(category, result.getCategory());
        assertNull(result.getWishItem());
        assertNull(result.getCreditCard());
        assertNull(result.getDueDay());
        assertEquals(user, result.getUser());
        verify(getCategoryCase).execute(user, categoryId);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para criação com categoria: deve criar despesa recorrente com categoria quando categoryId fornecido
    @Test
    void addExpenseRecurring_deveCriarDespesaRecorrenteComCategoria_quandoCategoryIdFornecido() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa com Categoria");
        params.setValue(new BigDecimal("150.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        verify(getCategoryCase).execute(user, categoryId);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para criação com item da lista de desejos: deve adicionar wishItem quando wishItemId fornecido
    @Test
    void addExpenseRecurring_deveAdicionarWishItem_quandoWishItemIdFornecido() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Long wishItemId = 20L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        WishListItem wishItem = WishListItemFixture.umWishListItemComId(wishItemId, user, category);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa com Wish Item");
        params.setValue(new BigDecimal("200.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setWishItemId(wishItemId);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getWishListItemCase.execute(user, wishItemId)).thenReturn(wishItem);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertEquals(wishItem, result.getWishItem());
        verify(getCategoryCase).execute(user, categoryId);
        verify(getWishListItemCase).execute(user, wishItemId);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para criação com dia de vencimento: deve definir dueDay quando dueDay fornecido
    @Test
    void addExpenseRecurring_deveDefinirDueDay_quandoDueDayFornecido() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        Integer dueDay = 15;
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa com Due Day");
        params.setValue(new BigDecimal("300.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setDueDay(dueDay);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertNotNull(result.getDueDay());
        assertEquals(dueDay, result.getDueDayValue());
        verify(getCategoryCase).execute(user, categoryId);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para criação com cartão de crédito: deve adicionar cartão de crédito quando creditCardId fornecido
    @Test
    void addExpenseRecurring_deveAdicionarCartaoCredito_quandoCreditCardIdFornecido() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Long creditCardId = 30L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        CreditCard creditCard = CreditCardFixture.umCartaoComId(creditCardId, user);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa com Cartão");
        params.setValue(new BigDecimal("400.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setCreditCardId(creditCardId);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getCreditCardCase.execute(user, creditCardId)).thenReturn(creditCard);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertEquals(creditCard, result.getCreditCard());
        verify(getCategoryCase).execute(user, categoryId);
        verify(getCreditCardCase).execute(user, creditCardId);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para dueDay do cartão: deve usar o billDueDay do cartão quando cartão possui billDueDay
    @Test
    void addExpenseRecurring_deveUsarBillDueDayDoCartao_quandoCartaoPossuiBillDueDay() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Long creditCardId = 30L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        DayOfMonth billDueDay = new DayOfMonth(25);
        CreditCard creditCard = CreditCardFixture.umCartaoComIdEBillDueDay(creditCardId, user, billDueDay);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa com Cartão e Bill Due Day");
        params.setValue(new BigDecimal("500.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setCreditCardId(creditCardId);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getCreditCardCase.execute(user, creditCardId)).thenReturn(creditCard);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertEquals(creditCard, result.getCreditCard());
        assertEquals(billDueDay, result.getDueDay());
        verify(getCategoryCase).execute(user, categoryId);
        verify(getCreditCardCase).execute(user, creditCardId);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para endDate com utilService: deve definir endDate usando utilService quando endDate fornecido
    @Test
    void addExpenseRecurring_deveDefinirEndDateComUtilService_quandoEndDateFornecido() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        LocalDateTime endDate = LocalDateTime.now().plusMonths(6);
        LocalDateTime processedEndDate = endDate.withDayOfMonth(endDate.toLocalDate().lengthOfMonth());
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa com End Date");
        params.setValue(new BigDecimal("600.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setEndDate(endDate);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(processedEndDate);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertEquals(processedEndDate, result.getEndDate());
        verify(getCategoryCase).execute(user, categoryId);
        verify(utilService).getLastDayMonthReference(endDate);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para cenário completo: deve criar despesa recorrente com todos os parâmetros preenchidos
    @Test
    void addExpenseRecurring_deveCriarDespesaRecorrenteCompleta_quandoTodosParametrosPreenchidos() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Long wishItemId = 20L;
        Long creditCardId = 30L;
        Integer dueDay = 10;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusMonths(12);
        LocalDateTime processedEndDate = endDate.withDayOfMonth(endDate.toLocalDate().lengthOfMonth());
        
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        WishListItem wishItem = WishListItemFixture.umWishListItemComId(wishItemId, user, category);
        CreditCard creditCard = CreditCardFixture.umCartaoComId(creditCardId, user);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa Completa");
        params.setValue(new BigDecimal("700.00"));
        params.setStartDate(startDate);
        params.setEndDate(endDate);
        params.setCategoryId(categoryId);
        params.setWishItemId(wishItemId);
        params.setCreditCardId(creditCardId);
        params.setDueDay(dueDay);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getWishListItemCase.execute(user, wishItemId)).thenReturn(wishItem);
        when(getCreditCardCase.execute(user, creditCardId)).thenReturn(creditCard);
        when(utilService.getLastDayMonthReference(endDate)).thenReturn(processedEndDate);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals("Despesa Completa", result.getDescription());
        assertEquals(new BigDecimal("700.00"), result.getValue());
        assertEquals(startDate, result.getStartDate());
        assertEquals(processedEndDate, result.getEndDate());
        assertEquals(category, result.getCategory());
        assertEquals(wishItem, result.getWishItem());
        assertEquals(creditCard, result.getCreditCard());
        assertEquals(dueDay, result.getDueDayValue());
        assertEquals(user, result.getUser());
        
        verify(getCategoryCase).execute(user, categoryId);
        verify(getWishListItemCase).execute(user, wishItemId);
        verify(getCreditCardCase).execute(user, creditCardId);
        verify(utilService).getLastDayMonthReference(endDate);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para precedência do billDueDay: deve usar billDueDay do cartão em vez do dueDay fornecido
    @Test
    void addExpenseRecurring_deveUsarBillDueDayDoCartao_quandoCartaoTemBillDueDayEDueDayFornecido() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Long creditCardId = 30L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        Integer dueDayFornecido = 15;
        DayOfMonth billDueDayCartao = new DayOfMonth(25);
        
        CreditCard creditCard = CreditCardFixture.umCartaoComIdEBillDueDay(creditCardId, user, billDueDayCartao);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Teste Precedência Due Day");
        params.setValue(new BigDecimal("800.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setCreditCardId(creditCardId);
        params.setDueDay(dueDayFornecido);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getCreditCardCase.execute(user, creditCardId)).thenReturn(creditCard);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertEquals(creditCard, result.getCreditCard());
        assertEquals(billDueDayCartao, result.getDueDay());
        assertNotEquals(dueDayFornecido, result.getDueDayValue());
        assertEquals(billDueDayCartao.getValue(), result.getDueDayValue());
        verify(getCategoryCase).execute(user, categoryId);
        verify(getCreditCardCase).execute(user, creditCardId);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para exceção categoria obrigatória: deve lançar exceção quando categoria for obrigatória mas não fornecida
    @Test
    void addExpenseRecurring_deveLancarExcecao_quandoCategoriaForObrigatoriaENaoFornecida() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa sem Categoria");
        params.setValue(new BigDecimal("100.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(null);

        // When/Then
        assertThrows(Exception.class, () -> addExpenseRecurringCase.execute(user, params));
        
        // Verificar que getCategoryCase não foi chamado
        verify(getCategoryCase, never()).execute(any(User.class), any(Long.class));
        verify(expenseRecurringRepository, never()).save(any(ExpenseRecurring.class));
    }

    // Teste para não adicionar wishItem: deve não chamar getWishListItemCase quando wishItemId é null
    @Test
    void addExpenseRecurring_naoDeveChamarGetWishListItemCase_quandoWishItemIdNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa sem Wish Item");
        params.setValue(new BigDecimal("100.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setWishItemId(null);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertNull(result.getWishItem());
        verify(getCategoryCase).execute(user, categoryId);
        verify(getWishListItemCase, never()).execute(any(User.class), any(Long.class));
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para não adicionar cartão: deve não chamar getCreditCardCase quando creditCardId é null
    @Test
    void addExpenseRecurring_naoDeveChamarGetCreditCardCase_quandoCreditCardIdNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa sem Cartão");
        params.setValue(new BigDecimal("100.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setCreditCardId(null);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertNull(result.getCreditCard());
        verify(getCategoryCase).execute(user, categoryId);
        verify(getCreditCardCase, never()).execute(any(User.class), any(Long.class));
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para não definir dueDay: deve não definir dueDay quando dueDay é null
    @Test
    void addExpenseRecurring_naoDeveDefinirDueDay_quandoDueDayNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa sem Due Day");
        params.setValue(new BigDecimal("100.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setDueDay(null);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertNull(result.getDueDay());
        verify(getCategoryCase).execute(user, categoryId);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para não processar endDate: deve não chamar utilService quando endDate é null
    @Test
    void addExpenseRecurring_naoDeveChamarUtilService_quandoEndDateNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa sem End Date");
        params.setValue(new BigDecimal("100.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);
        params.setEndDate(null);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        assertNull(result.getEndDate());
        verify(getCategoryCase).execute(user, categoryId);
        verify(utilService, never()).getLastDayMonthReference(any(LocalDateTime.class));
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }

    // Teste para verificar que o repository save é sempre chamado no final
    @Test
    void addExpenseRecurring_deveChamarRepositorySave_sempre() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Long categoryId = 10L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setDescription("Despesa para verificar save");
        params.setValue(new BigDecimal("50.00"));
        params.setStartDate(LocalDateTime.now());
        params.setCategoryId(categoryId);

        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ExpenseRecurring result = addExpenseRecurringCase.execute(user, params);

        // Then
        assertNotNull(result);
        verify(expenseRecurringRepository).save(any(ExpenseRecurring.class));
    }
}
