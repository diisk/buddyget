package br.dev.diisk.application.transaction.expense.cases;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.credit_card.cases.GetCreditCardCase;
import br.dev.diisk.application.goal.cases.GetGoalCase;
import br.dev.diisk.application.monthly_summary.cases.AddMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.transaction.expense.dtos.AddExpenseTransactionParams;
import br.dev.diisk.application.wish_list.cases.GetWishListItemCase;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;
import br.dev.diisk.domain.transaction.expense.ExpenseRecurring;
import br.dev.diisk.domain.transaction.expense.ExpenseTransaction;
import br.dev.diisk.domain.transaction.expense.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.WishListItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Classe utilitária de fixtures para instanciar entidades reais com todos os campos obrigatórios preenchidos
class Fixture {
    static User umUsuario(Long id) {
        User user = new User("Usuário Teste", null, "senha", null);
        user.setId(id);
        return user;
    }

    static Category umaCategoria(Long id, User user) {
        Category c = new Category(user, "Categoria Teste", "desc", "icon", CategoryTypeEnum.EXPENSE,
                new HexadecimalColor("#123456"));
        c.setId(id);
        return c;
    }

    static CreditCard umCartao(Long id, User user) {
        CreditCard cc = new CreditCard(user, "Cartão Teste", new DayOfMonth(1), new DayOfMonth(2), BigDecimal.TEN,
                new HexadecimalColor("#654321"));
        cc.setId(id);
        return cc;
    }

    static WishListItem umWishItem(Long id, User user, Category cat) {
        WishListItem w = new WishListItem(user, "Loja Teste", BigDecimal.TEN, cat);
        w.setId(id);
        return w;
    }

    static ExpenseRecurring umaRecorrencia(Long id, User user, Category cat) {
        ExpenseRecurring e = new ExpenseRecurring("desc",
                mock(br.dev.diisk.domain.shared.value_objects.DataRange.class), cat, BigDecimal.TEN,
                LocalDateTime.now(), user);
        e.setId(id);
        return e;
    }

    static Goal umaMeta(Long id, User user) {
        Goal g = new Goal(user, "Meta Teste", BigDecimal.TEN);
        g.setId(id);
        return g;
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

    @InjectMocks
    private AddExpenseTransactionCase addExpenseTransactionCase;

    // Caminho feliz: todos os campos preenchidos
    @Test
    void addExpenseTransaction_deveSalvarTransacaoComTodosOsCampos_quandoTodosOsDadosForemInformados() {
        // Dado
        Long userId = 10L, categoryId = 1L, creditCardId = 2L, wishItemId = 3L, expenseRecurringId = 4L, goalId = 5L;
        User user = Fixture.umUsuario(userId);
        Category category = Fixture.umaCategoria(categoryId, user);
        CreditCard creditCard = Fixture.umCartao(creditCardId, user);
        WishListItem wishItem = Fixture.umWishItem(wishItemId, user, category);
        ExpenseRecurring expenseRecurring = Fixture.umaRecorrencia(expenseRecurringId, user, category);
        Goal goal = Fixture.umaMeta(goalId, user);
        String description = "desc";
        BigDecimal value = BigDecimal.TEN;
        LocalDateTime paymentDate = LocalDateTime.now();
        LocalDateTime dueDate = paymentDate.plusDays(5);
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(dueDate);
        when(params.getWishItemId()).thenReturn(wishItemId);
        when(params.getExpenseRecurringId()).thenReturn(expenseRecurringId);
        when(params.getGoalId()).thenReturn(goalId);
        when(params.getCreditCardId()).thenReturn(creditCardId);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(getCreditCardCase.execute(user, creditCardId)).thenReturn(creditCard);
        when(getWishListItemCase.execute(user, wishItemId)).thenReturn(wishItem);
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(getGoalCase.execute(user, goalId)).thenReturn(goal);

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
        User user = Fixture.umUsuario(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        LocalDateTime paymentDate = LocalDateTime.now();
        Long categoryId = 1L;
        Category category = Fixture.umaCategoria(categoryId, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(paymentDate);
        when(params.getDueDate()).thenReturn(null);
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
        User user = Fixture.umUsuario(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Category category = Fixture.umaCategoria(categoryId, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(null);
        when(params.getDueDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando
        addExpenseTransactionCase.execute(user, params);

        // Então
        verify(addMonthlySummaryValueCase, never()).execute(any(), any());
    }

    // Deve permitir categoryId nulo
    @Test
    void addExpenseTransaction_devePermitirCategoryIdNulo_quandoCategoryIdNaoInformado() {
        // Dado
        User user = Fixture.umUsuario(10L);
        String description = "desc";
        BigDecimal value = BigDecimal.ONE;
        Long categoryId = 1L;
        Category category = Fixture.umaCategoria(categoryId, user);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        AddExpenseTransactionParams params = mock(AddExpenseTransactionParams.class);
        when(params.getDescription()).thenReturn(description);
        when(params.getCategoryId()).thenReturn(categoryId);
        when(params.getValue()).thenReturn(value);
        when(params.getPaymentDate()).thenReturn(LocalDateTime.now());
        when(params.getDueDate()).thenReturn(null);
        when(params.getWishItemId()).thenReturn(null);
        when(params.getExpenseRecurringId()).thenReturn(null);
        when(params.getGoalId()).thenReturn(null);
        when(params.getCreditCardId()).thenReturn(null);

        // Quando/Então: não lança exceção
        assertDoesNotThrow(() -> addExpenseTransactionCase.execute(user, params));
    }
}
