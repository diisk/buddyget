package br.dev.diisk.application.cases.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.dtos.expense.UpdateExpenseDto;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.mappers.expense.UpdateExpenseDtoToExpenseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;

public class UpdateExpenseCaseTest {

    @Mock
    private IExpenseRepository expenseRepository;

    @Mock
    private UpdateExpenseDtoToExpenseMapper mapper;

    @Mock
    private GetExpenseCase getExpenseCase;

    @InjectMocks
    private UpdateExpenseCase updateExpenseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateExpenseCase_quandoDadosValidos_DeveAtualizarDespesa() {
        // Given
        User user = new User();
        user.setId(1L);
        Long expenseId = 1L;

        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setType(CategoryTypeEnum.EXPENSE);
        newCategory.setUser(user);

        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setId(3L);
        newCreditCard.setUser(user);

        UpdateExpenseDto dto = new UpdateExpenseDto();
        dto.setDescription("Updated Description");
        dto.setAmount(BigDecimal.valueOf(200.00));
        dto.setCategoryId(newCategory.getId());
        dto.setCreditCardId(newCreditCard.getId());
        dto.setDueDate(LocalDateTime.now());
        dto.setPaymentDate(LocalDateTime.now().plusDays(1));

        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setUser(user);

        when(getExpenseCase.execute(user, expenseId)).thenReturn(expense);
        doAnswer(invocation -> {
            Expense updatedExpense = invocation.getArgument(2);
            updatedExpense.setDescription(dto.getDescription());
            updatedExpense.setAmount(dto.getAmount());
            updatedExpense.setCategory(newCategory);
            updatedExpense.setCreditCard(newCreditCard);
            updatedExpense.setDueDate(dto.getDueDate());
            updatedExpense.setPaymentDate(dto.getPaymentDate());
            return null;
        }).when(mapper).update(user, dto, expense);

        when(expenseRepository.save(expense)).thenReturn(expense);

        // When
        Expense result = updateExpenseCase.execute(user, expenseId, dto);

        // Then
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(dto.getCreditCardId(), result.getCreditCard().getId());
        assertEquals(dto.getDueDate(), result.getDueDate());
        assertEquals(dto.getPaymentDate(), result.getPaymentDate());
    }

    @Test
    public void updateExpenseCase_quandoCategoriaInvalida_DeveLancarExcecao() {
        // Given
        User user = new User();
        user.setId(1L);
        Long expenseId = 1L;

        Category invalidCategory = new Category();
        invalidCategory.setId(2L);
        invalidCategory.setType(CategoryTypeEnum.INCOME); // Invalid type
        invalidCategory.setUser(user);

        UpdateExpenseDto dto = new UpdateExpenseDto();
        dto.setCategoryId(invalidCategory.getId());

        Expense expense = new Expense();
        expense.setCategory(invalidCategory);

        when(getExpenseCase.execute(user, expenseId)).thenReturn(expense);
        doAnswer(invocation -> {
            Expense updatedExpense = invocation.getArgument(2);
            updatedExpense.setCategory(invalidCategory);
            return null;
        }).when(mapper).update(user, dto, expense);

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            updateExpenseCase.execute(user, expenseId, dto);
        });
    }

    @Test
    public void updateExpenseCase_quandoDescricaoNula_DeveAtualizarSemErro() {
        // Given
        User user = new User();
        user.setId(1L);
        Long expenseId = 1L;

        Category category = new Category();
        category.setId(2L);
        category.setType(CategoryTypeEnum.EXPENSE);
        category.setUser(user);

        UpdateExpenseDto dto = new UpdateExpenseDto();
        dto.setDescription(null); // Null description
        dto.setAmount(BigDecimal.valueOf(150.00));

        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setCategory(category);
        expense.setUser(user);

        when(getExpenseCase.execute(user, expenseId)).thenReturn(expense);
        doAnswer(invocation -> {
            Expense updatedExpense = invocation.getArgument(2);
            updatedExpense.setAmount(dto.getAmount());
            return null;
        }).when(mapper).update(user, dto, expense);

        when(expenseRepository.save(expense)).thenReturn(expense);

        // When
        Expense result = updateExpenseCase.execute(user, expenseId, dto);

        // Then
        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(null, result.getDescription());
    }

    @Test
    public void updateExpenseCase_quandoCreditCardIdMenosUm_DeveRemoverCartaoDeCredito() {
        // Given
        User user = new User();
        user.setId(1L);
        Long expenseId = 1L;

        Category category = new Category();
        category.setId(2L);
        category.setType(CategoryTypeEnum.EXPENSE);
        category.setUser(user);

        UpdateExpenseDto dto = new UpdateExpenseDto();
        dto.setDescription("Updated Description");
        dto.setAmount(BigDecimal.valueOf(200.00));
        dto.setCategoryId(category.getId());
        dto.setCreditCardId(-1L); // No credit card
        dto.setDueDate(LocalDateTime.now());
        dto.setPaymentDate(LocalDateTime.now().plusDays(1));

        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setUser(user);
        expense.setCategory(category);
        expense.setCreditCard(new CreditCard()); // Existing credit card

        when(getExpenseCase.execute(user, expenseId)).thenReturn(expense);
        doAnswer(invocation -> {
            Expense updatedExpense = invocation.getArgument(2);
            updatedExpense.setDescription(dto.getDescription());
            updatedExpense.setAmount(dto.getAmount());
            updatedExpense.setCategory(category);
            updatedExpense.setCreditCard(null); // Remove credit card
            updatedExpense.setDueDate(dto.getDueDate());
            updatedExpense.setPaymentDate(dto.getPaymentDate());
            return null;
        }).when(mapper).update(user, dto, expense);

        when(expenseRepository.save(expense)).thenReturn(expense);

        // When
        Expense result = updateExpenseCase.execute(user, expenseId, dto);

        // Then
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(null, result.getCreditCard()); // No credit card
        assertEquals(dto.getDueDate(), result.getDueDate());
        assertEquals(dto.getPaymentDate(), result.getPaymentDate());
    }
}
