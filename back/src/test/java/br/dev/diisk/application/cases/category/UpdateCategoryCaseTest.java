package br.dev.diisk.application.cases.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.dev.diisk.application.dtos.category.UpdateCategoryDTO;
import br.dev.diisk.application.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.entities.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;

import java.util.Optional;

public class UpdateCategoryCaseTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private GetCategoryCase getCategoryCase;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UpdateCategoryCase updateCategoryCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateCategoryCase_quandoCategoriaJaExiste_DeveLancarExcecao() {
        // Given
        Long userId = 1L;
        Long categoryId = 1L;
        UpdateCategoryDTO dto = new UpdateCategoryDTO();
        dto.setDescription("New Description");
        dto.setActive(false);

        User user = new User();
        user.setId(userId);
        Category category = new Category();
        category.setId(categoryId);
        category.setUser(user);
        category.setType(CategoryTypeEnum.INCOME);

        Category existingCategory = new Category();
        existingCategory.setId(2L);

        when(getCategoryCase.execute(userId, categoryId)).thenReturn(category);
        when(categoryRepository.findBy(userId, category.getType(), dto.getDescription())).thenReturn(Optional.of(existingCategory));

        // When & Then
        assertThrows(ValueAlreadyInDatabaseException.class, () -> {
            updateCategoryCase.execute(userId, categoryId, dto);
        });
    }

    @Test
    public void updateCategoryCase_quandoDadosValidos_DeveAtualizarCategoria() {
        // Given
        Long userId = 1L;
        Long categoryId = 1L;
        UpdateCategoryDTO dto = new UpdateCategoryDTO();
        dto.setDescription("New Description");
        dto.setActive(false);

        User user = new User();
        user.setId(userId);
        Category category = new Category();
        category.setId(categoryId);
        category.setUser(user);
        category.setType(CategoryTypeEnum.INCOME);

        when(getCategoryCase.execute(userId, categoryId)).thenReturn(category);
        when(categoryRepository.findBy(userId, category.getType(), dto.getDescription())).thenReturn(Optional.empty());
        doAnswer(invocation -> {
            Category c = invocation.getArgument(1);
            c.setDescription(dto.getDescription());
            c.setActive(dto.getActive());
            return null;
        }).when(mapper).map(dto, category);

        // When
        Category updatedCategory = updateCategoryCase.execute(userId, categoryId, dto);

        // Then
        assertEquals(dto.getDescription(), updatedCategory.getDescription());
        assertEquals(dto.getActive(), updatedCategory.getActive());
    }
}
