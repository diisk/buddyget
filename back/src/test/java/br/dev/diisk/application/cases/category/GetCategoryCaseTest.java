package br.dev.diisk.application.cases.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;

import java.util.Optional;

public class GetCategoryCaseTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private GetCategoryCase getCategoryCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCategoryCase_quandoCategoriaNaoExiste_DeveLancarExcecao() {
        // Given
        Long userId = 1L;
        Long categoryId = 1L;
        User user = new User();
        user.setId(userId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DbValueNotFoundException.class, () -> {
            getCategoryCase.execute(user, categoryId);
        });
    }

    @Test
    public void getCategoryCase_quandoCategoriaNaoPertenceAoUsuario_DeveLancarExcecao() {
        // Given
        Long userId = 1L;
        Long categoryId = 1L;
        User user = new User();
        user.setId(userId);
        User anotherUser = new User();
        anotherUser.setId(2L);
        Category category = new Category();
        category.setUser(anotherUser);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When & Then
        assertThrows(DbValueNotFoundException.class, () -> {
            getCategoryCase.execute(user, categoryId);
        });
    }

    @Test
    public void getCategoryCase_quandoCategoriaExiste_DeveRetornarCategoria() {
        // Given
        Long userId = 1L;
        Long categoryId = 1L;
        User user = new User();
        user.setId(userId);
        Category category = new Category();
        category.setUser(user);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Category result = getCategoryCase.execute(user, categoryId);

        // Then
        assertEquals(category, result);
    }
}
