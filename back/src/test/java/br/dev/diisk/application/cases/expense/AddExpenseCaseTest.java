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

import br.dev.diisk.application.dtos.expense.AddExpenseDto;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.mappers.expense.AddExpenseDtoToExpenseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;

public class AddExpenseCaseTest {

    @Mock
    private IExpenseRepository expenseRepository;

    @Mock
    private AddExpenseDtoToExpenseMapper mapper;

    @InjectMocks
    private AddExpenseCase addExpenseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addExpenseCase_quandoDadosValidos_DeveAdicionarDespesa() {
        // Given
        User user = new User();
        user.setId(1L);
        Category category = new Category();
        category.setId(2L);
        category.setType(CategoryTypeEnum.EXPENSE);
        category.setUser(user);
        CreditCard creditCard = new CreditCard();
        creditCard.setId(3L);
        creditCard.setUser(user);
        AddExpenseDto dto = new AddExpenseDto();
        dto.setDescription("Test Description");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setCategoryId(category.getId());
        dto.setCreditCardId(creditCard.getId());
        dto.setDueDate(LocalDateTime.now());
        dto.setPaymentDate(LocalDateTime.now().plusDays(1));

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setDescription(dto.getDescription());
        expense.setCategory(category);
        expense.setCreditCard(creditCard);
        expense.setDueDate(dto.getDueDate());
        expense.setPaymentDate(dto.getPaymentDate());
        expense.setAmount(dto.getAmount());

        when(mapper.apply(user, dto)).thenReturn(expense);
        doAnswer(invocation -> {
            Expense savedExpense = invocation.getArgument(0);
            savedExpense.setId(1L);
            return null;
        }).when(expenseRepository).save(expense);

        // When
        Expense result = addExpenseCase.execute(user, dto);

        // Then
        assertEquals(1L, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(dto.getCreditCardId(), result.getCreditCard().getId());
        assertEquals(dto.getDueDate(), result.getDueDate());
        assertEquals(dto.getPaymentDate(), result.getPaymentDate());
        assertEquals(dto.getAmount(), result.getAmount());
    }

    @Test
    public void addExpenseCase_quandoCategoriaInvalida_DeveLancarDbValueNotFoundException() {
        // Given
        User user = new User();
        user.setId(1L);
        AddExpenseDto dto = new AddExpenseDto();
        dto.setDescription("Test Description");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setCategoryId(1L);

        Category invalidCategory = new Category();
        invalidCategory.setId(1L);
        invalidCategory.setType(CategoryTypeEnum.INCOME); // Invalid type
        invalidCategory.setUser(user);

        Expense expense = new Expense();
        expense.setCategory(invalidCategory);

        when(mapper.apply(user, dto)).thenReturn(expense);

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            addExpenseCase.execute(user, dto);
        });
    }
}
