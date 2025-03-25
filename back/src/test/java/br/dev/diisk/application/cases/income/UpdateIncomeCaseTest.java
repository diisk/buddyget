package br.dev.diisk.application.cases.income;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.cases.category.GetCategoryCase;
import br.dev.diisk.application.dtos.income.UpdateIncomeDto;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.mappers.income.UpdateIncomeDtoToIncomeMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;

public class UpdateIncomeCaseTest {

    @Mock
    private UpdateIncomeDtoToIncomeMapper mapper;

    @Mock
    private IIncomeRepository incomeRepository;

    @Mock
    private GetIncomeCase getIncomeCase;

    @InjectMocks
    private UpdateIncomeCase updateIncomeCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateIncomeCase_quandoDadosValidos_DeveAtualizarRenda() {
        // Given
        Long userId = 1L;
        Long incomeId = 1L;
        User user = new User();
        user.setId(userId);
        Category category = new Category();
        category.setId(1L);

        Category newCategory = new Category();
        category.setId(2L);
        
        Income existingIncome = new Income();
        existingIncome.setId(incomeId);
        existingIncome.setUser(user);
        existingIncome.setCategory(category);

        UpdateIncomeDto dto = new UpdateIncomeDto();
        dto.setDescription("Updated Income");
        dto.setAmount(new BigDecimal("2000.00"));
        dto.setCategoryId(newCategory.getId());
        dto.setReceiptDate(LocalDateTime.now());
        

        when(getIncomeCase.execute(user, incomeId)).thenReturn(existingIncome);
        doAnswer(invocation -> {
            UpdateIncomeDto source = invocation.getArgument(1);
            Income destination = invocation.getArgument(2);
            destination.setDescription(source.getDescription());
            destination.setAmount(source.getAmount());
            destination.setReceiptDate(source.getReceiptDate());
            destination.setCategory(newCategory);
            return null;
        }).when(mapper).update(user, dto, existingIncome);

        when(incomeRepository.save(existingIncome)).thenReturn(existingIncome);

        // When
        Income result = updateIncomeCase.execute(user, incomeId, dto);

        // Then
        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getReceiptDate(), result.getReceiptDate());
        assertEquals(dto.getCategoryId(), result.getCategory().getId());
    }

    @Test
    public void updateIncomeCase_quandoCategoriaNaoEncontrada_DeveLancarDbValueNotFoundException() {
        // Given
        Long userId = 1L;
        Long incomeId = 1L;
        Income existingIncome = new Income();
        User user = new User();
        user.setId(userId);
        UpdateIncomeDto dto = new UpdateIncomeDto();
        dto.setDescription("Updated Income");
        dto.setAmount(new BigDecimal("2000.00"));
        dto.setCategoryId(999L); // Non-existent category ID
        dto.setReceiptDate(LocalDateTime.now());

        when(getIncomeCase.execute(user, incomeId)).thenReturn(existingIncome);

        doThrow(new DbValueNotFoundException(GetCategoryCase.class, "categoryId")).when(mapper).update(user, dto, existingIncome);

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> {
            updateIncomeCase.execute(user, incomeId, dto);
        });
    }
}
