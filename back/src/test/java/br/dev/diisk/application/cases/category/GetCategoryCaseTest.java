package br.dev.diisk.application.cases.category;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso GetCategoryCase.
 * Segue o padrão Given-When-Then e cobre cenários felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class GetCategoryCaseTest {

    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private User user;
    @InjectMocks
    private GetCategoryCase getCategoryCase;

    private final Long categoryId = 1L;
    private Category category;

    @BeforeEach
    void setUp() {
        category = mock(Category.class);
    }

    /**
     * Deve retornar a categoria corretamente quando encontrada e pertence ao usuário.
     */
    @Test
    @DisplayName("getCategoryCase_deveRetornarCategoria_quandoCategoriaPertenceAoUsuario")
    void getCategoryCase_deveRetornarCategoria_quandoCategoriaPertenceAoUsuario() {
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        // When
        Category result = getCategoryCase.execute(user, categoryId);
        // Then
        assertNotNull(result);
        assertEquals(category, result);
        verify(categoryRepository).findById(categoryId);
    }

    /**
     * Deve lançar exceção quando a categoria não for encontrada.
     */
    @Test
    @DisplayName("getCategoryCase_deveLancarExcecao_quandoCategoriaNaoEncontrada")
    void getCategoryCase_deveLancarExcecao_quandoCategoriaNaoEncontrada() {
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        // When / Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getCategoryCase.execute(user, categoryId));
        assertTrue(ex.getDetails().get("valor").equals(categoryId.toString()));
    }

    /**
     * Deve lançar exceção quando a categoria não pertence ao usuário.
     */
    @Test
    @DisplayName("getCategoryCase_deveLancarExcecao_quandoCategoriaNaoPertenceAoUsuario")
    void getCategoryCase_deveLancarExcecao_quandoCategoriaNaoPertenceAoUsuario() {
        // Given
        when(category.getUserId()).thenReturn(999L); // id diferente
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        // When / Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getCategoryCase.execute(user, categoryId));
        assertTrue(ex.getDetails().get("valor").equals(categoryId.toString()));
    }

    /**
     * Deve lançar exceção quando o usuário for nulo.
     */
    @Test
    @DisplayName("getCategoryCase_deveLancarExcecao_quandoUsuarioForNulo")
    void getCategoryCase_deveLancarExcecao_quandoUsuarioForNulo() {
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        // When / Then
        assertThrows(NullPointerException.class, () -> getCategoryCase.execute(null, categoryId));
    }
}
