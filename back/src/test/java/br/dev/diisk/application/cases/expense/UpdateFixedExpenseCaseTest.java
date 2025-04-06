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
import br.dev.diisk.application.dtos.expense.UpdateFixedExpenseDto;
import br.dev.diisk.application.exceptions.ExcessiveOptionalFieldException;
import br.dev.diisk.application.exceptions.RequiredFieldException;
import br.dev.diisk.application.exceptions.date.PeriodOrderException;
import br.dev.diisk.application.exceptions.date.PeriodRangeLesserException;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.mappers.expense.UpdateFixedExpenseDtoToFixedExpenseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.enums.expense.FixedExpenseTypeEnum;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;
import br.dev.diisk.infra.services.MessageService;

public class UpdateFixedExpenseCaseTest {

    @Mock
    private IFixedExpenseRepository fixedExpenseRepository;

    @Mock
    private UpdateFixedExpenseDtoToFixedExpenseMapper mapper;

    @Mock
    private GetFixedExpenseCase getFixedExpenseCase;

    @Mock
    private UtilService utilService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private UpdateFixedExpenseCase updateFixedExpenseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateFixedExpenseCase_quandoDadosValidosComAlteracaoDataInicial_deveAtualizarDespesaFixa() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        Category newCategory = new Category();
        newCategory.setId(1L);
        newCategory.setType(CategoryTypeEnum.EXPENSE);
        newCategory.setUser(user);

        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setId(1L);
        newCreditCard.setUser(user);

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setDescription("Updated Description");
        dto.setTotalAmount(BigDecimal.valueOf(500.00));
        dto.setCategoryId(newCategory.getId());
        dto.setCreditCardId(newCreditCard.getId());
        dto.setDueDay(15);
        dto.setStartReference(LocalDateTime.now().minusMonths(1));

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setType(FixedExpenseTypeEnum.DEADLINE);
        fixedExpense.setId(fixedExpenseId);
        fixedExpense.setTotalInstallments(5);
        fixedExpense.setStartReference(LocalDateTime.now().minusMonths(4));
        fixedExpense.setEndReference(LocalDateTime.now());
        fixedExpense.setUser(user);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);
        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.getMonthsBetweenReferences(dto.getStartReference(),
                fixedExpense.getEndReference())).thenReturn(1);

        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(2);
            expense.setDescription(dto.getDescription());
            expense.setCategory(newCategory);
            expense.setCreditCard(newCreditCard);
            expense.setDueDay(dto.getDueDay());
            expense.setStartReference(dto.getStartReference());
            return null;
        }).when(mapper).update(user, dto, fixedExpense);

        // When
        FixedExpense result = updateFixedExpenseCase.execute(user, fixedExpenseId, dto);

        // Then
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(dto.getCreditCardId(), result.getCreditCard().getId());
        assertEquals(dto.getDueDay(), result.getDueDay());
        assertEquals(dto.getStartReference(), result.getStartReference());
        assertEquals(2, result.getTotalInstallments());
    }

    @Test
    public void updateFixedExpenseCase_quandoDadosValidosComAlteracaoParcela_deveAtualizarDespesaFixa() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        Category newCategory = new Category();
        newCategory.setId(1L);
        newCategory.setType(CategoryTypeEnum.EXPENSE);
        newCategory.setUser(user);

        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setId(1L);
        newCreditCard.setUser(user);

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setDescription("Updated Description");
        dto.setTotalAmount(BigDecimal.valueOf(500.00));
        dto.setCategoryId(newCategory.getId());
        dto.setCreditCardId(newCreditCard.getId());
        dto.setDueDay(15);
        dto.setTotalInstallments(3);

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setType(FixedExpenseTypeEnum.DEADLINE);
        fixedExpense.setId(fixedExpenseId);
        fixedExpense.setTotalInstallments(5);
        fixedExpense.setStartReference(LocalDateTime.now().minusMonths(4));
        fixedExpense.setEndReference(LocalDateTime.now());
        fixedExpense.setUser(user);

        LocalDateTime newEndReference = fixedExpense.getStartReference().plusMonths(dto.getTotalInstallments() - 1);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);
        when(utilService.toReference(fixedExpense.getStartReference())).thenReturn(fixedExpense.getStartReference());
        when(utilService.toReference(
                fixedExpense.getStartReference().plusMonths(dto.getTotalInstallments() - 1), true))
                .thenReturn(newEndReference);

        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(2);
            expense.setDescription(dto.getDescription());
            expense.setCategory(newCategory);
            expense.setCreditCard(newCreditCard);
            expense.setDueDay(dto.getDueDay());
            expense.setTotalInstallments(dto.getTotalInstallments());
            return null;
        }).when(mapper).update(user, dto, fixedExpense);

        // When
        FixedExpense result = updateFixedExpenseCase.execute(user, fixedExpenseId, dto);

        // Then
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(dto.getCreditCardId(), result.getCreditCard().getId());
        assertEquals(dto.getDueDay(), result.getDueDay());
        assertEquals(newEndReference, result.getEndReference());
        assertEquals(dto.getTotalInstallments(), result.getTotalInstallments());
    }

    @Test
    public void updateFixedExpenseCase_quandoDadosValidosComAlteracaoDataInicialEParcela_deveAtualizarDespesaFixa() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        Category newCategory = new Category();
        newCategory.setId(1L);
        newCategory.setType(CategoryTypeEnum.EXPENSE);
        newCategory.setUser(user);

        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setId(1L);
        newCreditCard.setUser(user);

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setDescription("Updated Description");
        dto.setTotalAmount(BigDecimal.valueOf(500.00));
        dto.setCategoryId(newCategory.getId());
        dto.setCreditCardId(newCreditCard.getId());
        dto.setDueDay(15);
        dto.setStartReference(LocalDateTime.now().minusMonths(1));
        dto.setTotalInstallments(3);

        LocalDateTime newEndReference = dto.getStartReference().plusMonths(dto.getTotalInstallments() - 1);

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setType(FixedExpenseTypeEnum.DEADLINE);
        fixedExpense.setId(fixedExpenseId);
        fixedExpense.setTotalInstallments(5);
        fixedExpense.setStartReference(LocalDateTime.now().minusMonths(4));
        fixedExpense.setEndReference(LocalDateTime.now());
        fixedExpense.setUser(user);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);
        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(
                dto.getStartReference().plusMonths(dto.getTotalInstallments() - 1), true)).thenReturn(newEndReference);

        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(2);
            expense.setDescription(dto.getDescription());
            expense.setCategory(newCategory);
            expense.setCreditCard(newCreditCard);
            expense.setDueDay(dto.getDueDay());
            expense.setStartReference(dto.getStartReference());
            expense.setTotalInstallments(dto.getTotalInstallments());
            return null;
        }).when(mapper).update(user, dto, fixedExpense);

        // When
        FixedExpense result = updateFixedExpenseCase.execute(user, fixedExpenseId, dto);

        // Then
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
        assertEquals(dto.getCreditCardId(), result.getCreditCard().getId());
        assertEquals(dto.getDueDay(), result.getDueDay());
        assertEquals(dto.getStartReference(), result.getStartReference());
        assertEquals(newEndReference, result.getEndReference());
        assertEquals(dto.getTotalInstallments(), result.getTotalInstallments());
    }

    @Test
    public void updateFixedExpenseCase_quandoDataFinalAntesDaInicial_deveLancarExcecao() {
        // Given
        User user = new User();
        user.setId(1L);
        Category category = new Category();
        category.setType(CategoryTypeEnum.EXPENSE);
        Long fixedExpenseId = 1L;
        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setStartReference(LocalDateTime.now());
        dto.setEndReference(LocalDateTime.now().minusMonths(1));

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setType(FixedExpenseTypeEnum.DEADLINE);
        fixedExpense.setCategory(category);
        fixedExpense.setStartReference(dto.getStartReference());
        fixedExpense.setEndReference(dto.getEndReference());

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);
        when(utilService.toReference(fixedExpense.getStartReference())).thenReturn(fixedExpense.getStartReference());
        when(utilService.toReference(fixedExpense.getEndReference(), true)).thenReturn(fixedExpense.getEndReference());
        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(2);
            expense.setStartReference(dto.getStartReference());
            expense.setEndReference(dto.getEndReference());
            return null;
        }).when(mapper).update(user, dto, fixedExpense);

        // When / Then
        assertThrows(PeriodOrderException.class, () -> {
            updateFixedExpenseCase.execute(user, fixedExpenseId, dto);
        });
    }

    @Test
    public void updateFixedExpenseCase_quandoDuracaoMenorQueUmMes_deveLancarExcecao() {
        // Given
        User user = new User();
        user.setId(1L);
        Category category = new Category();
        category.setType(CategoryTypeEnum.EXPENSE);
        Long fixedExpenseId = 1L;
        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setStartReference(LocalDateTime.now());
        dto.setTotalInstallments(1);

        LocalDateTime newEndReference = dto.getStartReference().plusMonths(dto.getTotalInstallments());

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setType(FixedExpenseTypeEnum.DEADLINE);
        fixedExpense.setCategory(category);
        fixedExpense.setStartReference(dto.getStartReference());
        fixedExpense.setTotalInstallments(dto.getTotalInstallments());

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);
        when(utilService.toReference(fixedExpense.getStartReference())).thenReturn(fixedExpense.getStartReference());
        when(utilService.toReference(
                fixedExpense.getStartReference().plusMonths(fixedExpense.getTotalInstallments() - 1), true))
                .thenReturn(newEndReference);
        when(messageService.getMessage("wording.expected")).thenReturn("Expected");
        when(messageService.getMessage("wording.months")).thenReturn("months");
        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(2);
            expense.setStartReference(dto.getStartReference());
            expense.setTotalInstallments(dto.getTotalInstallments());
            return null;
        }).when(mapper).update(user, dto, fixedExpense);

        // When / Then
        assertThrows(PeriodRangeLesserException.class, () -> {
            updateFixedExpenseCase.execute(user, fixedExpenseId, dto);
        });
    }

    @Test
    public void updateFixedExpenseCase_quandoCategoriaInvalida_deveLancarExcecao() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        Category invalidCategory = new Category();
        invalidCategory.setId(1L);
        invalidCategory.setType(CategoryTypeEnum.INCOME); // Invalid type
        invalidCategory.setUser(user);

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setCategoryId(invalidCategory.getId());

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setCategory(invalidCategory);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);
        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(2);
            expense.setCategory(invalidCategory);
            return null;
        }).when(mapper).update(user, dto, fixedExpense);

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            updateFixedExpenseCase.execute(user, fixedExpenseId, dto);
        });
    }

    @Test
    public void updateFixedExpenseCase_quandoAmbosAmountETotalAmount_deveLancarExcecao() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setAmount(new BigDecimal(500));
        dto.setTotalAmount(new BigDecimal(1000));

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setUser(user);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);

        // When / Then
        assertThrows(ExcessiveOptionalFieldException.class, () -> {
            updateFixedExpenseCase.execute(user, fixedExpenseId, dto);
        });
    }

    @Test
    public void updateFixedExpenseCase_quandoAmbosEndReferenceETotalInstallmentsPresentes_deveLancarExcecao() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setEndReference(LocalDateTime.now().plusMonths(1));
        dto.setTotalInstallments(3);

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setUser(user);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);

        // When / Then
        assertThrows(ExcessiveOptionalFieldException.class, () -> {
            updateFixedExpenseCase.execute(user, fixedExpenseId, dto);
        });
    }

    @Test
    public void updateFixedExpenseCase_quandoTipoMudadoParaUndefinedTime_deveAplicarValoresPadrao() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        Category category = new Category();
        category.setId(1L);
        category.setType(CategoryTypeEnum.EXPENSE);
        category.setUser(user);

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.UNDEFINED_TIME);
        dto.setAmount(BigDecimal.valueOf(100.00));

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setCategory(category);
        fixedExpense.setType(FixedExpenseTypeEnum.DEADLINE);
        fixedExpense.setStartReference(LocalDateTime.now());
        fixedExpense.setEndReference(LocalDateTime.now().plusMonths(3));
        fixedExpense.setTotalInstallments(4);
        fixedExpense.setUser(user);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);
        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(2);
            expense.setType(dto.getType());
            expense.setAmount(dto.getAmount());
            return null;
        }).when(mapper).update(user, dto, fixedExpense);

        // When
        FixedExpense result = updateFixedExpenseCase.execute(user, fixedExpenseId, dto);

        // Then
        assertEquals(FixedExpenseTypeEnum.UNDEFINED_TIME, result.getType());
        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(null, result.getEndReference());
        assertEquals(null, result.getTotalInstallments());
    }

    @Test
    public void updateFixedExpenseCase_quandoTipoMudadoParaDeadline_deveAplicarValoresPadrao() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        Category category = new Category();
        category.setId(1L);
        category.setType(CategoryTypeEnum.EXPENSE);
        category.setUser(user);

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.DEADLINE);
        dto.setStartReference(LocalDateTime.now());
        dto.setTotalInstallments(3);
        dto.setTotalAmount(BigDecimal.valueOf(300.00));

        LocalDateTime endReference = dto.getStartReference().plusMonths(dto.getTotalInstallments()-1);

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setCategory(category);
        fixedExpense.setType(FixedExpenseTypeEnum.UNDEFINED_TIME);
        fixedExpense.setUser(user);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);
        when(utilService.toReference(dto.getStartReference())).thenReturn(dto.getStartReference());
        when(utilService.toReference(dto.getStartReference().plusMonths(dto.getTotalInstallments() - 1), true))
                .thenReturn(dto.getStartReference().plusMonths(dto.getTotalInstallments() - 1));
        when(utilService.divide(dto.getTotalAmount(), BigDecimal.valueOf(dto.getTotalInstallments())))
                .thenReturn(BigDecimal.valueOf(100.00));
        doAnswer(invocation -> {
            FixedExpense expense = invocation.getArgument(2);
            expense.setType(dto.getType());
            expense.setTotalInstallments(dto.getTotalInstallments());
            expense.setStartReference(dto.getStartReference());
            return null;
        }).when(mapper).update(user, dto, fixedExpense);

        // When
        FixedExpense result = updateFixedExpenseCase.execute(user, fixedExpenseId, dto);

        // Then
        assertEquals(FixedExpenseTypeEnum.DEADLINE, result.getType());
        assertEquals(dto.getStartReference(), result.getStartReference());
        assertEquals(dto.getStartReference().plusMonths(dto.getTotalInstallments() - 1), result.getEndReference());
        assertEquals(dto.getTotalInstallments(), result.getTotalInstallments());
        assertEquals(endReference, result.getEndReference());
        assertEquals(BigDecimal.valueOf(100.00), result.getAmount());
    }

    @Test
    public void updateFixedExpenseCase_quandoAlteradoTipoUndefinedTimeSemAmount_deveLancarExcecao() {
        // Given
        User user = new User();
        user.setId(1L);
        Long fixedExpenseId = 1L;

        UpdateFixedExpenseDto dto = new UpdateFixedExpenseDto();
        dto.setType(FixedExpenseTypeEnum.UNDEFINED_TIME);

        FixedExpense fixedExpense = new FixedExpense();
        fixedExpense.setType(FixedExpenseTypeEnum.DEADLINE);
        fixedExpense.setUser(user);

        when(getFixedExpenseCase.execute(user, fixedExpenseId)).thenReturn(fixedExpense);

        // When / Then
        assertThrows(RequiredFieldException.class, () -> {
            updateFixedExpenseCase.execute(user, fixedExpenseId, dto);
        });
    }
}
