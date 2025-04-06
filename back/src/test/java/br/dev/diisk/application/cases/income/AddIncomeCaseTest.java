package br.dev.diisk.application.cases.income;

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

import br.dev.diisk.application.dtos.income.AddIncomeDto;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.mappers.income.AddIncomeDtoToIncomeMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;

public class AddIncomeCaseTest {

    @Mock
    private IIncomeRepository incomeRepository;

    @Mock
    private AddIncomeDtoToIncomeMapper mapper;

    @InjectMocks
    private AddIncomeCase addIncomeCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addIncomeCase_quandoDadosValidos_DeveAdicionarRenda() {
        // Given
        User user = new User();
        user.setId(1L);
        AddIncomeDto dto = new AddIncomeDto();
        dto.setDescription("Test Income");
        dto.setAmount(new BigDecimal("1000.00"));
        dto.setCategoryId(1L);
        dto.setReceiptDate(LocalDateTime.now());
        Category category = new Category();
        category.setId(1L);
        category.setType(CategoryTypeEnum.INCOME);
        category.setUser(user);
        Income newIncome = new Income();
        newIncome.setDescription(dto.getDescription());
        newIncome.setAmount(dto.getAmount());
        newIncome.setUser(user);
        newIncome.setReceiptDate(dto.getReceiptDate());
        newIncome.setCategory(category);

        when(mapper.apply(user, dto)).thenReturn(newIncome);
        doAnswer(invocation -> {
            Income income = invocation.getArgument(0);
            income.setId(1L);
            return null;
        }).when(incomeRepository).save(newIncome);

        // When
        Income income = addIncomeCase.execute(user, dto);

        // Then
        assertEquals(1L, income.getId());
        assertEquals(dto.getDescription(), income.getDescription());
        assertEquals(dto.getAmount(), income.getAmount());
        assertEquals(user, income.getUser());
        assertEquals(dto.getReceiptDate(), income.getReceiptDate());
        assertEquals(dto.getCategoryId(), income.getCategory().getId());
    }

    @Test
    public void addIncomeCase_quandoCategoriaComTipoDiferente_DeveLancarDbValueNotFoundException() {
        // Given
        User user = new User();
        user.setId(1L);
        AddIncomeDto dto = new AddIncomeDto();
        dto.setDescription("Test Income");
        dto.setAmount(new BigDecimal("1000.00"));
        dto.setCategoryId(1L);
        dto.setReceiptDate(LocalDateTime.now());
        Category category = new Category();
        category.setId(1L);
        category.setUser(user);
        category.setType(CategoryTypeEnum.EXPENSE);
        Income newIncome = new Income();
        newIncome.setCategory(category);

        when(mapper.apply(user, dto)).thenReturn(newIncome);

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            addIncomeCase.execute(user, dto);
        });
    }
}
