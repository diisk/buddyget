package br.dev.diisk.application.cases.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.UtilService;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;

import java.util.Collections;
import java.util.List;

public class GetCategoriesCaseTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private ListCategoriesCase getCategoriesCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCategoriesCase_quandoNaoExistemCategorias_DeveRetornarListaVazia() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        CategoryTypeEnum type = CategoryTypeEnum.INCOME;
        when(categoryRepository.findAllBy(userId, type)).thenReturn(Collections.emptyList());

        // When
        List<Category> categories = getCategoriesCase.execute(user, type);

        // Then
        assertEquals(0, categories.size());
    }

    @Test
    public void getCategoriesCase_quandoExistemCategorias_DeveRetornarListaDeCategorias() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        CategoryTypeEnum type = CategoryTypeEnum.INCOME;
        Category category = new Category();
        when(categoryRepository.findAllBy(userId, type)).thenReturn(List.of(category));

        // When
        List<Category> categories = getCategoriesCase.execute(user, type);

        // Then
        assertEquals(1, categories.size());
        assertEquals(category, categories.get(0));
    }
}
