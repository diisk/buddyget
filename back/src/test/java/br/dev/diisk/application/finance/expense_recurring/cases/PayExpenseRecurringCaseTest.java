package br.dev.diisk.application.finance.expense_recurring.cases;

import br.dev.diisk.application.finance.expense_recurring.dtos.PayExpenseRecurringParams;
import br.dev.diisk.application.finance.expense_transaction.cases.AddExpenseTransactionCase;
import br.dev.diisk.application.finance.expense_transaction.dtos.AddExpenseTransactionParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.CreditCardFixture;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurringFixture;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransactionFixture;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

/**
 * Testes unitários para o caso de uso PayExpenseRecurringCase.
 */
@ExtendWith(MockitoExtension.class)
class PayExpenseRecurringCaseTest {

    @Mock
    private GetExpenseRecurringCase getExpenseRecurringCase;

    @Mock
    private AddExpenseTransactionCase addExpenseTransactionCase;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private PayExpenseRecurringCase payExpenseRecurringCase;

    // Teste para o caminho feliz: deve criar transação com todos os dados quando
    // parâmetros válidos
    @Test
    void payExpenseRecurring_deveCriarTransacaoCorretamente_quandoParametrosValidos() {
        // Given
        Long expenseRecurringId = 1L;
        LocalDateTime paymentDate = LocalDateTime.of(2023, 10, 15, 10, 0);
        LocalDateTime referenceDate = LocalDateTime.of(2023, 10, 1, 0, 0);

        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId);
        ExpenseTransaction expectedTransaction = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);

        PayExpenseRecurringParams params = new PayExpenseRecurringParams(
                paymentDate, referenceDate);

        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(utilService.getFirstDayMonthReference(referenceDate)).thenReturn(referenceDate);
        when(addExpenseTransactionCase.execute(eq(user), any(AddExpenseTransactionParams.class)))
                .thenReturn(expectedTransaction);

        // When
        ExpenseTransaction result = payExpenseRecurringCase.execute(user, expenseRecurringId, params);

        // Then
        assertEquals(expectedTransaction, result);
        verify(getExpenseRecurringCase).execute(user, expenseRecurringId);

        ArgumentCaptor<AddExpenseTransactionParams> paramsCaptor = ArgumentCaptor
                .forClass(AddExpenseTransactionParams.class);
        verify(addExpenseTransactionCase).execute(eq(user), paramsCaptor.capture());

        AddExpenseTransactionParams capturedParams = paramsCaptor.getValue();
        assertEquals(expenseRecurring.getCategoryId(), capturedParams.getCategoryId());
        assertEquals(expenseRecurring.getDescription(), capturedParams.getDescription());
        assertEquals(expenseRecurring.getValue(), capturedParams.getValue());
        assertEquals(paymentDate, capturedParams.getPaymentDate());
        assertEquals(referenceDate, capturedParams.getRecurringReferenceDate());
        assertEquals(expenseRecurringId, capturedParams.getExpenseRecurringId());
        assertNull(capturedParams.getCreditCardId());
        assertNull(capturedParams.getWishItemId());
        assertNull(capturedParams.getDueDate());
    }

    // Teste para o caminho feliz com cartão de crédito: deve incluir ID do cartão
    // nos parâmetros
    @Test
    void payExpenseRecurring_deveIncluirCartaoCredito_quandoExpenseRecurringTemCartao() {
        // Given
        Long expenseRecurringId = 1L;
        Long creditCardId = 5L;
        LocalDateTime paymentDate = LocalDateTime.of(2023, 10, 15, 10, 0);
        LocalDateTime referenceDate = LocalDateTime.of(2023, 10, 1, 0, 0);

        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        CreditCard creditCard = CreditCardFixture.umCartaoComId(creditCardId, user);

        // Criar ExpenseRecurring com o mesmo usuário
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId);
        // Substituir o usuário da fixture para garantir consistência
        try {
            java.lang.reflect.Field userField = br.dev.diisk.domain.shared.entities.UserRastrableEntity.class
                    .getDeclaredField("user");
            userField.setAccessible(true);
            userField.set(expenseRecurring, user);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar usuário na fixture", e);
        }

        expenseRecurring.addCreditCard(creditCard);
        ExpenseTransaction expectedTransaction = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);

        PayExpenseRecurringParams params = new PayExpenseRecurringParams(
                paymentDate, referenceDate);

        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(addExpenseTransactionCase.execute(eq(user), any(AddExpenseTransactionParams.class)))
                .thenReturn(expectedTransaction);

        // When
        ExpenseTransaction result = payExpenseRecurringCase.execute(user, expenseRecurringId, params);

        // Then
        assertEquals(expectedTransaction, result);

        ArgumentCaptor<AddExpenseTransactionParams> paramsCaptor = ArgumentCaptor
                .forClass(AddExpenseTransactionParams.class);
        verify(addExpenseTransactionCase).execute(eq(user), paramsCaptor.capture());

        AddExpenseTransactionParams capturedParams = paramsCaptor.getValue();
        assertEquals(creditCardId, capturedParams.getCreditCardId());
    }

    // Teste para o caminho feliz com wish list item: deve incluir ID do item da
    // lista de desejos nos parâmetros
    @Test
    void payExpenseRecurring_deveIncluirWishListItem_quandoExpenseRecurringTemWishItem() {
        // Given
        Long expenseRecurringId = 1L;
        Long wishItemId = 10L;
        LocalDateTime paymentDate = LocalDateTime.of(2023, 10, 15, 10, 0);
        LocalDateTime referenceDate = LocalDateTime.of(2023, 10, 1, 0, 0);

        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        WishListItem wishItem = WishListItemFixture.umWishListItemComId(wishItemId, user, category);

        // Criar ExpenseRecurring com o mesmo usuário
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId);
        // Substituir o usuário da fixture para garantir consistência
        try {
            java.lang.reflect.Field userField = br.dev.diisk.domain.shared.entities.UserRastrableEntity.class
                    .getDeclaredField("user");
            userField.setAccessible(true);
            userField.set(expenseRecurring, user);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar usuário na fixture", e);
        }

        expenseRecurring.addWishItem(wishItem);
        ExpenseTransaction expectedTransaction = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);

        PayExpenseRecurringParams params = new PayExpenseRecurringParams(
                paymentDate, referenceDate);

        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(addExpenseTransactionCase.execute(eq(user), any(AddExpenseTransactionParams.class)))
                .thenReturn(expectedTransaction);

        // When
        ExpenseTransaction result = payExpenseRecurringCase.execute(user, expenseRecurringId, params);

        // Then
        assertEquals(expectedTransaction, result);

        ArgumentCaptor<AddExpenseTransactionParams> paramsCaptor = ArgumentCaptor
                .forClass(AddExpenseTransactionParams.class);
        verify(addExpenseTransactionCase).execute(eq(user), paramsCaptor.capture());

        AddExpenseTransactionParams capturedParams = paramsCaptor.getValue();
        assertEquals(wishItemId, capturedParams.getWishItemId());
    }

    // Teste para o caminho feliz com due day: deve calcular e incluir a data de
    // vencimento
    @Test
    void payExpenseRecurring_deveCalcularDueDate_quandoExpenseRecurringTemDueDay() {
        // Given
        Long expenseRecurringId = 1L;
        Integer dueDay = 20;
        LocalDateTime paymentDate = LocalDateTime.of(2023, 10, 15, 10, 0);
        LocalDateTime referenceDate = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime expectedDueDate = referenceDate.withDayOfMonth(dueDay);

        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);

        // Criar ExpenseRecurring com o mesmo usuário
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId);
        // Substituir o usuário da fixture para garantir consistência
        try {
            java.lang.reflect.Field userField = br.dev.diisk.domain.shared.entities.UserRastrableEntity.class
                    .getDeclaredField("user");
            userField.setAccessible(true);
            userField.set(expenseRecurring, user);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar usuário na fixture", e);
        }

        // Configurar o dueDay usando reflexão ou método setter se disponível
        try {
            java.lang.reflect.Field dueDayField = ExpenseRecurring.class.getDeclaredField("dueDay");
            dueDayField.setAccessible(true);
            dueDayField.set(expenseRecurring, new DayOfMonth(dueDay));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar dueDay na fixture", e);
        }

        ExpenseTransaction expectedTransaction = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category);

        PayExpenseRecurringParams params = new PayExpenseRecurringParams(
                paymentDate, referenceDate);

        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(addExpenseTransactionCase.execute(eq(user), any(AddExpenseTransactionParams.class)))
                .thenReturn(expectedTransaction);

        // When
        ExpenseTransaction result = payExpenseRecurringCase.execute(user, expenseRecurringId, params);

        // Then
        assertEquals(expectedTransaction, result);

        ArgumentCaptor<AddExpenseTransactionParams> paramsCaptor = ArgumentCaptor
                .forClass(AddExpenseTransactionParams.class);
        verify(addExpenseTransactionCase).execute(eq(user), paramsCaptor.capture());

        AddExpenseTransactionParams capturedParams = paramsCaptor.getValue();
        assertEquals(expectedDueDate, capturedParams.getDueDate());
    }

    // Teste para exceção: deve lançar NullPointerException quando paymentDate for
    // null
    @Test
    void payExpenseRecurring_deveLancarNullPointerException_quandoPaymentDateNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        PayExpenseRecurringParams params = new PayExpenseRecurringParams(
                null, LocalDateTime.now());

        // When & Then
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> payExpenseRecurringCase.execute(user, 1L, params));
        assertEquals("A data de pagamento é obrigatória", ex.getMessage());
        verifyNoInteractions(getExpenseRecurringCase);
        verifyNoInteractions(addExpenseTransactionCase);
    }

    // Teste para exceção: deve lançar NullPointerException quando referenceDate for
    // null
    @Test
    void payExpenseRecurring_deveLancarNullPointerException_quandoReferenceDateNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        PayExpenseRecurringParams params = new PayExpenseRecurringParams(
                LocalDateTime.now(), null);

        // When & Then
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> payExpenseRecurringCase.execute(user, 1L, params));
        assertEquals("A data de referência é obrigatória", ex.getMessage());
        verifyNoInteractions(getExpenseRecurringCase);
        verifyNoInteractions(addExpenseTransactionCase);
    }

    // Teste para exceção: deve lançar NullPointerException quando
    // expenseRecurringId for null
    @Test
    void payExpenseRecurring_deveLancarNullPointerException_quandoExpenseRecurringIdNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        PayExpenseRecurringParams params = new PayExpenseRecurringParams(
                LocalDateTime.now(), LocalDateTime.now());

        // When & Then
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> payExpenseRecurringCase.execute(user, null, params));
        assertEquals("O id da despesa recorrente é obrigatório", ex.getMessage());
        verifyNoInteractions(getExpenseRecurringCase);
        verifyNoInteractions(addExpenseTransactionCase);
    }
}
