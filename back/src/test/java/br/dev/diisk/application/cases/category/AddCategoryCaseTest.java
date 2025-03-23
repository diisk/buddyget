package br.dev.diisk.application.cases.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.UtilService;
import br.dev.diisk.application.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;

import java.util.Optional;

public class AddCategoryCaseTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private AddCategoryCase addCategoryCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addCategoryCase_quandoCategoriaJaExiste_DeveLancarExcecao() {
        // Given
        User user = new User();
        String description = "Test Category";
        CategoryTypeEnum type = CategoryTypeEnum.INCOME;
        Category existingCategory = new Category();
        existingCategory.setId(1L);

        when(categoryRepository.findBy(user.getId(), type, description)).thenReturn(Optional.of(existingCategory));

        // When & Then
        assertThrows(ValueAlreadyInDatabaseException.class, () -> {
            addCategoryCase.execute(user, description, type);
        });
    }

    @Test
    public void addCategoryCase_quandoDadosValidos_DeveAdicionarCategoria() {
        // Given
        User user = new User();
        String description = "Test Category";
        CategoryTypeEnum type = CategoryTypeEnum.INCOME;
        Category newCategory = new Category();

        when(categoryRepository.findBy(user.getId(), type, description)).thenReturn(Optional.of(newCategory));

        doAnswer(invocation->{
            Category category = invocation.getArgument(0);
            category.setId(1L);
            return category;
        }).when(categoryRepository).save(newCategory);

        // When
        Category category = addCategoryCase.execute(user, description, type);

        // Then
        assertEquals(1L, category.getId());
        assertEquals(description, category.getDescription());
        assertEquals(type, category.getType());
        assertEquals(user, category.getUser());
    }
}
