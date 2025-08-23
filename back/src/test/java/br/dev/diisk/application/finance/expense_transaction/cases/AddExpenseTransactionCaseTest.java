package br.dev.diisk.application.finance.expense_transaction.cases;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.credit_card.cases.GetCreditCardCase;
import br.dev.diisk.application.finance.expense_recurring.cases.GetExpenseRecurringCase;
import br.dev.diisk.application.finance.expense_transaction.dtos.AddExpenseTransactionParams;
import br.dev.diisk.application.goal.cases.GetGoalCase;
import br.dev.diisk.application.monthly_summary.cases.AddMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.application.wish_list.cases.GetWishListItemCase;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.CreditCardFixture;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurringFixture;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.goal.GoalFixture;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import br.dev.diisk.domain.wish_list.WishListItem;
import br.dev.diisk.domain.wish_list.WishListItemFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Classe utilitária de fixtures para instanciar entidades reais com todos os campos obrigatórios preenchidos
class Fixture {
    static User umUsuario(Long id) {
        return UserFixture.umUsuarioComId(id);
    }

    static Category umaCategoria(Long id, User user) {
        return CategoryFixture.umaCategoriaComId(id, CategoryTypeEnum.EXPENSE, user);
    }

    static CreditCard umCartao(Long id, User user) {
        return CreditCardFixture.umCartaoComId(id, user);
    }

    static WishListItem umWishItem(Long id, User user, Category cat) {
        return WishListItemFixture.umWishListItemComId(id, user, cat);
    }

    static Goal umaMeta(Long id, User user) {
        return GoalFixture.umGoalComId(id, user);
    }
}

@ExtendWith(MockitoExtension.class)
class AddExpenseTransactionCaseTest {
    @Mock
    private IExpenseTransactionRepository expenseRepository;
    @Mock
    private GetCategoryCase getCategoryCase;
    @Mock
    private GetGoalCase getGoalCase;
    @Mock
    private GetCreditCardCase getCreditCardCase;
    @Mock
    private GetWishListItemCase getWishListItemCase;
    @Mock
    private GetExpenseRecurringCase getExpenseRecurringCase;
    @Mock
    private AddMonthlySummaryValueCase addMonthlySummaryValueCase;
    @Mock
    private UtilService utilService;

    @InjectMocks
    private AddExpenseTransactionCase addExpenseTransactionCase;

    // Caminho feliz: todos os campos preenchidos
    @Test
    void addExpenseTransaction_deveSalvarTransacaoComTodosOsCampos_quandoTodosOsDadosForemInformados() {
        // Dado
        Long userId = 10L, categoryId = 1L, creditCardId = 2L, wishItemId = 3L, expenseRecurringId = 4L, goalId = 5L;
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        CreditCard creditCard = CreditCardFixture.umCartaoComId(creditCardId, user);
        WishListItem wishItem = WishListItemFixture.umWishListItemComId(wishItemId, user, category);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        Goal goal = GoalFixture.umGoalComId(goalId, user);
        String description = "desc";
        BigDecimal value = BigDecimal.TEN;
        LocalDateTime paymentDate = LocalDateTime.now();
        LocalDateTime dueDate = paymentDate.plusDays(5);
        LocalDateTime recurringReferenceDate = LocalDateTime.now();
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(dueDate);
        when(params.getRecurringReferenceDate()).thenReturn(recurringReferenceDate);
        when(params.getWishItemId()).thenReturn(wishItemId);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId);
        when(params.getGoalId()).thenReturn(goalId);
        when(params.getCreditCardId()).thenReturn(creditCardId);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getCreditCardCase.execute(user, creditCardId)).thenReturn(creditCard);
        when(getWishListItemCase.execute(user, wishItemId)).thenReturn(wishItem);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(getGoalCase.execute(user, goalId)).thenReturn(goal);
        
        // Mock do UtilService para período de vigência
        LocalDateTime currentDate = LocalDateTime.now();
        when(utilService.getFirstDayMonthReference(any())).thenReturn(currentDate.minusYears(1));
        when(utilService.getLastDayMonthReference(any())).thenReturn(currentDate.plusYears(1));

        // Quando
        addExpenseTransactionCase.execute(user, params);

        // Então
        ArgumentCaptor<ExpenseTransaction> captor = ArgumentCaptor.forClass(ExpenseTransaction.class);
        verify(expenseRepository).save(captor.capture());
        ExpenseTransaction saved = captor.getValue();
        assertEquals(description, saved.getDescription());
        assertEquals(category, saved.getCategory());
        assertEquals(value, saved.getValue());
        assertEquals(paymentDate, saved.getPaymentDate());
        assertEquals(user, saved.getUser());
        assertEquals(dueDate, saved.getDueDate());
        assertEquals(creditCard, saved.getCreditCard());
        assertEquals(expenseRecurring, saved.getExpenseRecurring());
        assertEquals(wishItem, saved.getWishItem());
        assertEquals(goal, saved.getGoal());
        verify(addMonthlySummaryValueCase).execute(eq(user), any(AddMonthlySummaryValueParams.class));
    }

    // Não deve adicionar relacionamentos opcionais se IDs forem nulos
    @Test
    void addExpenseTransaction_deveSalvarTransacaoSemRelacionamentosOpcionais_quandoIdsOpcionaisForemNulos() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        LocalDateTime paymentDate = LocalDateTime.now();
        Long categoryId = 1L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando
        addExpenseTransactionCase.execute(user, params);

        // Então
        ArgumentCaptor<ExpenseTransaction> captor = ArgumentCaptor.forClass(ExpenseTransaction.class);
        verify(expenseRepository).save(captor.capture());
        ExpenseTransaction saved = captor.getValue();
        assertEquals(description, saved.getDescription());
        assertNotNull(saved.getCategory());
        assertEquals(value, saved.getValue());
        assertEquals(paymentDate, saved.getPaymentDate());
        assertEquals(user, saved.getUser());
        assertNull(saved.getDueDate());
        assertNull(saved.getCreditCard());
        assertNull(saved.getExpenseRecurring());
        assertNull(saved.getWishItem());
        assertNull(saved.getGoal());
    }

    // Não deve atualizar resumo mensal se paymentDate for nulo
    @Test
    void addExpenseTransaction_naoDeveAtualizarResumoMensal_quandoPaymentDateForNulo() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(null);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando
        addExpenseTransactionCase.execute(user, params);

        // Então
        verify(addMonthlySummaryValueCase, never()).execute(any(), any());
    }

    // Deve lançar exceção quando categoryId for nulo (categoria é obrigatória)
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoCategoryIdNulo() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(null); // Categoria nula
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(LocalDateTime.now());
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção pois categoria é obrigatória
        assertThrows(Exception.class, () -> addExpenseTransactionCase.execute(user, params));
        
        // Verificar que getCategoryCase não foi chamado
        verifyNoInteractions(getCategoryCase);
    }

    // Deve lançar exceção se expenseRecurringId for informado mas recurringReferenceDate for nulo
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoExpenseRecurringIdInformadoMasRecurringReferenceDateNulo() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        LocalDateTime paymentDate = LocalDateTime.now();
        Long categoryId = 1L;
        Long expenseRecurringId = 4L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null); // NULL aqui é o problema
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId); // mas tem ID aqui
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> addExpenseTransactionCase.execute(user, params));
        assertEquals("A data da referência da recorrencia deve ser informada se a despesa for recorrente.", 
            exception.getMessage());
    }

    // Deve lançar exceção se expenseRecurringId for informado mas paymentDate for nulo
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoExpenseRecurringIdInformadoMasPaymentDateNulo() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long expenseRecurringId = 4L;
        LocalDateTime recurringReferenceDate = LocalDateTime.now();
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(null); // NULL aqui é o problema
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(recurringReferenceDate);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId); // mas tem ID aqui
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> addExpenseTransactionCase.execute(user, params));
        assertEquals("A data do pagamento deve ser informada se a despesa for recorrente.", 
            exception.getMessage());
    }

    // Deve lançar exceção se já existe transação no mês de referência
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoJaExisteTransacaoNoMesDeReferencia() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long expenseRecurringId = 4L;
        LocalDateTime paymentDate = LocalDateTime.now();
        LocalDateTime recurringReferenceDate = LocalDateTime.of(2024, 6, 15, 10, 0);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        // Simular transação existente no mesmo mês/ano
        ExpenseTransaction existingTransaction = mock(ExpenseTransaction.class);
        when(existingTransaction.getRecurringReferenceDate()).thenReturn(LocalDateTime.of(2024, 6, 20, 15, 30));
        
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(expenseRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
            .thenReturn(List.of(existingTransaction));
        
        // Mock do UtilService para período de vigência  
        when(utilService.getFirstDayMonthReference(any())).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0));
        when(utilService.getLastDayMonthReference(any())).thenReturn(LocalDateTime.of(2024, 12, 31, 23, 59, 59));
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(recurringReferenceDate);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> addExpenseTransactionCase.execute(user, params));
        assertEquals("Já existe uma despesa vinculada a essa recorrência na referência informada.", 
            exception.getMessage());
    }

    // Deve permitir criar transação recorrente quando não há conflito no mês de referência
    @Test
    void addExpenseTransaction_devePermitirCriarTransacaoRecorrente_quandoNaoHaConflitoNoMesDeReferencia() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long expenseRecurringId = 4L;
        LocalDateTime paymentDate = LocalDateTime.now();
        LocalDateTime recurringReferenceDate = LocalDateTime.of(2024, 6, 15, 10, 0);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        // Simular transação existente em mês/ano diferente
        ExpenseTransaction existingTransaction = mock(ExpenseTransaction.class);
        when(existingTransaction.getRecurringReferenceDate()).thenReturn(LocalDateTime.of(2024, 5, 20, 15, 30));
        
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(expenseRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
            .thenReturn(List.of(existingTransaction));
        
        // Mock do UtilService para período de vigência  
        when(utilService.getFirstDayMonthReference(any())).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0));
        when(utilService.getLastDayMonthReference(any())).thenReturn(LocalDateTime.of(2024, 12, 31, 23, 59, 59));
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(recurringReferenceDate);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: não deve lançar exceção
        assertDoesNotThrow(() -> addExpenseTransactionCase.execute(user, params));
        
        // Verificar que a transação foi salva
        verify(expenseRepository).save(any(ExpenseTransaction.class));
    }

    // Deve chamar addMonthlySummaryValueCase com parâmetros corretos quando paymentDate não é nulo
    @Test
    void addExpenseTransaction_deveChamarAddMonthlySummaryValue_quandoPaymentDateNaoNulo() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.TEN;
        Long categoryId = 1L;
        LocalDateTime paymentDate = LocalDateTime.of(2024, 6, 15, 10, 0);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando
        addExpenseTransactionCase.execute(user, params);

        // Então - verificar que addMonthlySummaryValueCase foi chamado com parâmetros corretos
        ArgumentCaptor<AddMonthlySummaryValueParams> captor = ArgumentCaptor.forClass(AddMonthlySummaryValueParams.class);
        verify(addMonthlySummaryValueCase).execute(eq(user), captor.capture());
        
        AddMonthlySummaryValueParams capturedParams = captor.getValue();
        assertEquals(6, capturedParams.getMonth()); // Junho
        assertEquals(2024, capturedParams.getYear());
        assertEquals(value, capturedParams.getValue());
        assertEquals(category, capturedParams.getCategory());
    }

    // Deve adicionar dueDate quando fornecido
    @Test
    void addExpenseTransaction_deveAdicionarDueDate_quandoDueDateFornecido() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        LocalDateTime paymentDate = LocalDateTime.now();
        LocalDateTime dueDate = paymentDate.plusDays(5);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(dueDate);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando
        ExpenseTransaction result = addExpenseTransactionCase.execute(user, params);

        // Então - verificar que dueDate foi adicionado
        assertEquals(dueDate, result.getDueDate());
    }

    // Deve adicionar creditCard quando fornecido
    @Test
    void addExpenseTransaction_deveAdicionarCreditCard_quandoCreditCardFornecido() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long creditCardId = 2L;
        LocalDateTime paymentDate = LocalDateTime.now();
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        CreditCard creditCard = Fixture.umCartao(creditCardId, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getCreditCardCase.execute(user, creditCardId)).thenReturn(creditCard);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(creditCardId);

        // Quando
        ExpenseTransaction result = addExpenseTransactionCase.execute(user, params);

        // Então - verificar que creditCard foi adicionado
        assertEquals(creditCard, result.getCreditCard());
    }

    // Deve adicionar wishItem quando fornecido
    @Test
    void addExpenseTransaction_deveAdicionarWishItem_quandoWishItemFornecido() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long wishItemId = 3L;
        LocalDateTime paymentDate = LocalDateTime.now();
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        WishListItem wishItem = Fixture.umWishItem(wishItemId, user, category);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getWishListItemCase.execute(user, wishItemId)).thenReturn(wishItem);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(wishItemId);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando
        ExpenseTransaction result = addExpenseTransactionCase.execute(user, params);

        // Então - verificar que wishItem foi adicionado
        assertEquals(wishItem, result.getWishItem());
    }

    // Deve adicionar goal quando fornecido
    @Test
    void addExpenseTransaction_deveAdicionarGoal_quandoGoalFornecido() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long goalId = 5L;
        LocalDateTime paymentDate = LocalDateTime.now();
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        Goal goal = Fixture.umaMeta(goalId, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getGoalCase.execute(user, goalId)).thenReturn(goal);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(goalId);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando
        ExpenseTransaction result = addExpenseTransactionCase.execute(user, params);

        // Então - verificar que goal foi adicionado
        assertEquals(goal, result.getGoal());
    }

    // Deve adicionar expenseRecurring com recurringReferenceDate quando fornecidos
    @Test
    void addExpenseTransaction_deveAdicionarExpenseRecurring_quandoExpenseRecurringEReferenceDateFornecidos() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long expenseRecurringId = 4L;
        LocalDateTime paymentDate = LocalDateTime.now();
        LocalDateTime recurringReferenceDate = LocalDateTime.of(2024, 6, 15, 10, 0);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(expenseRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
            .thenReturn(List.of()); // Lista vazia - sem conflitos
        
        // Mock do UtilService para período de vigência  
        when(utilService.getFirstDayMonthReference(any())).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0));
        when(utilService.getLastDayMonthReference(any())).thenReturn(LocalDateTime.of(2024, 12, 31, 23, 59, 59));
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(recurringReferenceDate);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando
        ExpenseTransaction result = addExpenseTransactionCase.execute(user, params);

        // Então - verificar que expenseRecurring foi adicionado
        assertEquals(expenseRecurring, result.getExpenseRecurring());
        assertEquals(recurringReferenceDate, result.getRecurringReferenceDate());
    }

    // Deve lançar exceção quando descrição for vazia
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoDescricaoVazia() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(""); // Descrição vazia
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(LocalDateTime.now());
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        assertThrows(Exception.class, () -> addExpenseTransactionCase.execute(user, params));
    }

    // Deve lançar exceção quando descrição for nula
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoDescricaoNula() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(null); // Descrição nula
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(LocalDateTime.now());
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        assertThrows(Exception.class, () -> addExpenseTransactionCase.execute(user, params));
    }

    // Deve lançar exceção quando valor for zero
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoValorZero() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        Long categoryId = 1L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(BigDecimal.ZERO); // Valor zero
        when(params.getPaymentDate()).thenReturn(LocalDateTime.now());
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        assertThrows(Exception.class, () -> addExpenseTransactionCase.execute(user, params));
    }

    // Deve lançar exceção quando valor for negativo
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoValorNegativo() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        Long categoryId = 1L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(new BigDecimal("-50.00")); // Valor negativo
        when(params.getPaymentDate()).thenReturn(LocalDateTime.now());
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        assertThrows(Exception.class, () -> addExpenseTransactionCase.execute(user, params));
    }

    // Deve lançar exceção quando valor for nulo
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoValorNulo() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        Long categoryId = 1L;
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(null); // Valor nulo
        when(params.getPaymentDate()).thenReturn(LocalDateTime.now());
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        assertThrows(Exception.class, () -> addExpenseTransactionCase.execute(user, params));
    }

    // Deve lançar exceção quando recurringReferenceDate está antes do período de vigência
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoRecurringReferenceDateAntesDoPeridoVigencia() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long expenseRecurringId = 4L;
        LocalDateTime paymentDate = LocalDateTime.of(2024, 6, 15, 10, 0);
        LocalDateTime recurringReferenceDate = LocalDateTime.of(2024, 3, 15, 10, 0); // Antes do período
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        LocalDateTime startReference = LocalDateTime.of(2024, 4, 1, 0, 0);
        LocalDateTime endReference = LocalDateTime.of(2024, 8, 31, 23, 59, 59);
        
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(utilService.getFirstDayMonthReference(any())).thenReturn(startReference);
        when(utilService.getLastDayMonthReference(any())).thenReturn(endReference);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(recurringReferenceDate);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> addExpenseTransactionCase.execute(user, params));
        assertEquals("A data da referência da recorrência deve estar entre o período de vigência da mesma.", 
            exception.getMessage());
    }

    // Deve lançar exceção quando recurringReferenceDate está depois do período de vigência
    @Test
    void addExpenseTransaction_deveLancarExcecao_quandoRecurringReferenceDateDepoisDoPeridoVigencia() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long expenseRecurringId = 4L;
        LocalDateTime paymentDate = LocalDateTime.of(2024, 6, 15, 10, 0);
        LocalDateTime recurringReferenceDate = LocalDateTime.of(2024, 10, 15, 10, 0); // Depois do período
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        LocalDateTime startReference = LocalDateTime.of(2024, 4, 1, 0, 0);
        LocalDateTime endReference = LocalDateTime.of(2024, 8, 31, 23, 59, 59);
        
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(utilService.getFirstDayMonthReference(any())).thenReturn(startReference);
        when(utilService.getLastDayMonthReference(any())).thenReturn(endReference);
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(recurringReferenceDate);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: deve lançar exceção
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> addExpenseTransactionCase.execute(user, params));
        assertEquals("A data da referência da recorrência deve estar entre o período de vigência da mesma.", 
            exception.getMessage());
    }

    // Deve permitir criar transação recorrente quando recurringReferenceDate está dentro do período de vigência
    @Test
    void addExpenseTransaction_devePermitirCriarTransacaoRecorrente_quandoRecurringReferenceDateDentroDoPeridoVigencia() {
        // Dado
        User user = UserFixture.umUsuarioComId(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Long expenseRecurringId = 4L;
        LocalDateTime paymentDate = LocalDateTime.of(2024, 6, 15, 10, 0);
        LocalDateTime recurringReferenceDate = LocalDateTime.of(2024, 6, 15, 10, 0); // Dentro do período
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        LocalDateTime startReference = LocalDateTime.of(2024, 4, 1, 0, 0);
        LocalDateTime endReference = LocalDateTime.of(2024, 8, 31, 23, 59, 59);
        
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(utilService.getFirstDayMonthReference(any())).thenReturn(startReference);
        when(utilService.getLastDayMonthReference(any())).thenReturn(endReference);
        when(expenseRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
            .thenReturn(List.of()); // Lista vazia - sem conflitos
        
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
        when(params.getRecurringReferenceDate()).thenReturn(recurringReferenceDate);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: não deve lançar exceção
        assertDoesNotThrow(() -> addExpenseTransactionCase.execute(user, params));
        
        // Verificar que a transação foi salva
        verify(expenseRepository).save(any(ExpenseTransaction.class));
    }
}
