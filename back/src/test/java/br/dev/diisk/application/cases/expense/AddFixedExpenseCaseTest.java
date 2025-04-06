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

import br.dev.diisk.application.UtilService;
import br.dev.diisk.application.dtos.expense.AddFixedExpenseDto;
import br.dev.diisk.application.exceptions.ExcessiveOptionalFieldException;
import br.dev.diisk.application.exceptions.RequiredFieldException;
import br.dev.diisk.application.exceptions.RequiredOptionalFieldException;
import br.dev.diisk.application.exceptions.date.PeriodOrderException;
import br.dev.diisk.application.exceptions.date.PeriodRangeLesserException;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.mappers.expense.AddFixedExpenseDtoToFixedExpenseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.enums.expense.FixedExpenseTypeEnum;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;
import br.dev.diisk.infra.services.MessageService;

public class AddFixedExpenseCaseTest {

    @Mock
    private IFixedExpenseRepository fixedExpenseRepository;

    @Mock
    private AddFixedExpenseDtoToFixedExpenseMapper mapper;

    @Mock
    private UtilService utilService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private AddFixedExpenseCase addFixedExpenseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addFixedExpenseCase_quandoDadosValidosComParcelas_DeveAdicionarDespesaFixa() {
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
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.DEADLINE);
        dto.setDescription("Test Description");
        dto.setTotalAmount(new BigDecimal("1200.00"));
        dto.setCategoryId(category.getId());
        dto.setCreditCardId(creditCard.getId());
        dto.setDueDay(15);
        dto.setStartReference(LocalDateTime.now());
        dto.setTotalInstallments(12);
        LocalDateTime endReference = dto.getStartReference().plusMonths(dto.getTotalInstallments()-1);
        BigDecimal installmentAmount = new BigDecimal("100.00");

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setUser(user);
        fixedExpense.setDescription(dto.getDescription());
        fixedExpense.setCategory(category);
        fixedExpense.setCreditCard(creditCard);
        fixedExpense.setDueDay(dto.getDueDay());
        fixedExpense.setStartReference(dto.getStartReference());
        fixedExpense.setTotalInstallments(dto.getTotalInstallments());
        fixedExpense.setEndReference(endReference);
        fixedExpense.setAmount(installmentAmount);


        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(endReference,true)).thenReturn(endReference);
        when(utilService.divide(dto.getTotalAmount(), new BigDecimal(dto.getTotalInstallments())))
            .thenReturn(installmentAmount);
        when(mapper.apply(user, dto)).thenReturn(fixedExpense);
        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(0);
            expense.setId(1L);
            return null;
        }).when(fixedExpenseRepository).save(fixedExpense);

        // When
        FixedExpense result = addFixedExpenseCase.execute(user, dto);

        // Then
        assertEquals(1L, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(dto.getCreditCardId(), result.getCreditCard().getId());
        assertEquals(dto.getDueDay(), result.getDueDay());
        assertEquals(dto.getStartReference(), result.getStartReference());
        assertEquals(dto.getTotalInstallments(), result.getTotalInstallments());
        assertEquals(endReference, dto.getEndReference());
        assertEquals(installmentAmount, result.getAmount());
        assertEquals(dto.getEndReference(), result.getEndReference());
    }

    @Test
    public void addFixedExpenseCase_quandoDadosValidosComDataFinal_DeveAdicionarDespesaFixa() {
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
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.DEADLINE);
        dto.setDescription("Test Description");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setCategoryId(category.getId());
        dto.setCreditCardId(creditCard.getId());
        dto.setDueDay(15);
        dto.setStartReference(LocalDateTime.now());
        dto.setEndReference(LocalDateTime.now().plusMonths(1));

        LocalDateTime newEndReference = dto.getEndReference();

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setUser(user);
        fixedExpense.setDescription(dto.getDescription());
        fixedExpense.setCategory(category);
        fixedExpense.setCreditCard(creditCard);
        fixedExpense.setDueDay(dto.getDueDay());
        fixedExpense.setStartReference(dto.getStartReference());
        fixedExpense.setTotalInstallments(2);
        fixedExpense.setEndReference(newEndReference);
        fixedExpense.setAmount(dto.getAmount());

        


        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(dto.getEndReference(),true)).thenReturn(newEndReference);
        when(utilService.getMonthsBetweenReferences(dto.getStartReference(), newEndReference)).thenReturn(1);
        when(mapper.apply(user, dto)).thenReturn(fixedExpense);
        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(0);
            expense.setId(1L);
            return null;
        }).when(fixedExpenseRepository).save(fixedExpense);

        // When
        FixedExpense result = addFixedExpenseCase.execute(user, dto);

        // Then
        assertEquals(1L, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(dto.getCreditCardId(), result.getCreditCard().getId());
        assertEquals(dto.getDueDay(), result.getDueDay());
        assertEquals(dto.getStartReference(), result.getStartReference());
        assertEquals(dto.getTotalInstallments(), result.getTotalInstallments());
        assertEquals(dto.getEndReference(), result.getEndReference());
    }

    @Test
    public void addFixedExpenseCase_quandoDadosValidosSemParcelaSemDataFinal_DeveAdicionarDespesaFixa() {
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
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.UNDEFINED_TIME);
        dto.setDescription("Test Description");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setCategoryId(category.getId());
        dto.setCreditCardId(creditCard.getId());
        dto.setDueDay(15);
        dto.setStartReference(LocalDateTime.now());

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setUser(user);
        fixedExpense.setDescription(dto.getDescription());
        fixedExpense.setCategory(category);
        fixedExpense.setCreditCard(creditCard);
        fixedExpense.setDueDay(dto.getDueDay());
        fixedExpense.setStartReference(dto.getStartReference());
        fixedExpense.setAmount(dto.getAmount());


        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(dto.getEndReference())).thenReturn(dto.getEndReference());
        when(utilService.getMonthsBetweenReferences(dto.getStartReference(), dto.getEndReference())).thenReturn(1);
        when(mapper.apply(user, dto)).thenReturn(fixedExpense);
        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(0);
            expense.setId(1L);
            return null;
        }).when(fixedExpenseRepository).save(fixedExpense);

        // When
        FixedExpense result = addFixedExpenseCase.execute(user, dto);

        // Then
        assertEquals(1L, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(dto.getCreditCardId(), result.getCreditCard().getId());
        assertEquals(dto.getDueDay(), result.getDueDay());
        assertEquals(dto.getStartReference(), result.getStartReference());
        assertEquals(dto.getEndReference(), null);
        assertEquals(dto.getTotalInstallments(), null);
        assertEquals(result.getAmount(), dto.getAmount());
        assertEquals(result.getTotalInstallments(), null);
        assertEquals(result.getEndReference(), null);
    }

    @Test
    public void addFixedExpenseCase_quandoCategoriaInvalida_DeveLancarDbValueNotFoundException() {
        // Given
        User user = new User();
        user.setId(1L);
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.UNDEFINED_TIME);
        dto.setAmount(new BigDecimal(500));
        dto.setCategoryId(1L);

        Category invalidCategory = new Category();
        invalidCategory.setId(1L);
        invalidCategory.setType(CategoryTypeEnum.INCOME);
        invalidCategory.setUser(user);

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setCategory(invalidCategory);

        when(mapper.apply(user, dto)).thenReturn(fixedExpense);

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            addFixedExpenseCase.execute(user, dto);
        });
    }


    @Test
    public void addFixedExpenseCase_quandoTipoIndefinidoSemAmount_DeveLancarExcecao() {
        // Given
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.UNDEFINED_TIME);
        dto.setAmount(null);

        // When / Then
        assertThrows(RequiredFieldException.class, () -> {
            addFixedExpenseCase.execute(new User(), dto);
        });
    }

    @Test
    public void addFixedExpenseCase_quandoTipoPrazoComInstallmentsMenorQueDois_DeveLancarExcecao() {
        // Given
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.DEADLINE);
        dto.setStartReference(LocalDateTime.now());
        dto.setAmount(new BigDecimal(500));
        dto.setTotalInstallments(1);
        LocalDateTime endReference = dto.getStartReference().plusMonths(dto.getTotalInstallments() - 1);

        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(endReference, true)).thenReturn(endReference);
        when(messageService.getMessage("wording.expected")).thenReturn("Expected");
        when(messageService.getMessage("wording.months")).thenReturn("months");

        // When / Then
        assertThrows(PeriodRangeLesserException.class, () -> {
            addFixedExpenseCase.execute(new User(), dto);
        });
    }

    @Test
    public void addFixedExpenseCase_quandoTipoPrazoSemEndReferenceEInstallments_DeveLancarExcecao() {
        // Given
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.DEADLINE);
        dto.setStartReference(LocalDateTime.now());
        dto.setEndReference(null);
        dto.setTotalInstallments(null);

        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());

        // When / Then
        assertThrows(RequiredOptionalFieldException.class, () -> {
            addFixedExpenseCase.execute(new User(), dto);
        });
    }

    @Test
    public void addFixedExpenseCase_quandoTipoPrazoComAmbosAmountETotalAmount_DeveLancarExcecao() {
        // Given
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.DEADLINE);
        dto.setStartReference(LocalDateTime.now());
        dto.setAmount(new BigDecimal("100.00"));
        dto.setTotalAmount(new BigDecimal("200.00"));

        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());

        // When / Then
        assertThrows(ExcessiveOptionalFieldException.class, () -> {
            addFixedExpenseCase.execute(new User(), dto);
        });
    }

    @Test
    public void addFixedExpenseCase_quandoTipoPrazoComAmbosEndReferenceETotalInstallments_DeveLancarExcecao() {
        // Given
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.DEADLINE);
        dto.setStartReference(LocalDateTime.now());
        dto.setEndReference(LocalDateTime.now().plusMonths(1));
        dto.setTotalInstallments(2);

        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(dto.getEndReference())).thenReturn(dto.getEndReference());

        // When / Then
        assertThrows(ExcessiveOptionalFieldException.class, () -> {
            addFixedExpenseCase.execute(new User(), dto);
        });
    }

    @Test
    public void addFixedExpenseCase_quandoTipoPrazoSemAmountETotalAmount_DeveLancarExcecao() {
        // Given
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.DEADLINE);
        dto.setStartReference(LocalDateTime.now());
        dto.setEndReference(LocalDateTime.now().plusMonths(1));
        dto.setAmount(null);
        dto.setTotalAmount(null);

        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(dto.getEndReference())).thenReturn(dto.getEndReference());

        // When / Then
        assertThrows(RequiredOptionalFieldException.class, () -> {
            addFixedExpenseCase.execute(new User(), dto);
        });
    }

    @Test
    public void addFixedExpenseCase_quandoDatasForaDeOrdem_DeveLancarExcecao() {
        // Given
        AddFixedExpenseDto dto = new AddFixedExpenseDto();
        dto.setStartReference(LocalDateTime.now());
        dto.setEndReference(LocalDateTime.now().minusMonths(1));

        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(dto.getEndReference())).thenReturn(dto.getEndReference());

        // When / Then
        assertThrows(PeriodOrderException.class, () -> {
            addFixedExpenseCase.execute(new User(), dto);
        });
    }

}
