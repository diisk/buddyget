package br.dev.diisk.application.cases.category;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.category.cases.UpdateCategoryCase;
import br.dev.diisk.application.category.dtos.UpdateCategoryParams;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import br.dev.diisk.domain.value_objects.HexadecimalColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso UpdateCategoryCase.
 * Segue o padrão Given-When-Then e cobre cenários felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class UpdateCategoryCaseTest {

    @Mock
    private GetCategoryCase getCategoryCase;
    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private User user;
    @Mock
    private Category category;
    @InjectMocks
    private UpdateCategoryCase updateCategoryCase;

    private final Long categoryId = 1L;
    private final String name = "Nova Categoria";
    private final String description = "Descrição atualizada";
    private final String iconName = "icon-novo";
    private final String color = "#123456";

    @BeforeEach
    void setUp() {
        updateCategoryCase = new UpdateCategoryCase(getCategoryCase, categoryRepository);
    }

    // Teste: deve atualizar categoria corretamente quando dados são válidos
    @Test
    @DisplayName("updateCategory_deveAtualizarCategoriaCorretamente_quandoDadosValidos")
    void updateCategory_deveAtualizarCategoriaCorretamente_quandoDadosValidos() {
        // Given
        UpdateCategoryParams params = new UpdateCategoryParams(description, name, color, iconName);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(category.update(name, description, iconName, new HexadecimalColor(color))).thenReturn(true);
        // When
        Category result = updateCategoryCase.execute(user, categoryId, params);
        // Then
        assertNotNull(result);
        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    // Teste: não deve salvar se nenhum campo for alterado
    @Test
    @DisplayName("updateCategory_naoDeveSalvar_quandoNadaAlterado")
    void updateCategory_naoDeveSalvar_quandoNadaAlterado() {
        // Given
        UpdateCategoryParams params = new UpdateCategoryParams(description, name, color, iconName);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(category.update(name, description, iconName, new HexadecimalColor(color))).thenReturn(false);
        // When
        Category result = updateCategoryCase.execute(user, categoryId, params);
        // Then
        assertNotNull(result);
        assertEquals(category, result);
        verify(categoryRepository, never()).save(category);
    }

    // Teste: deve atualizar apenas campos não nulos
    @Test
    @DisplayName("updateCategory_deveAtualizarApenasCamposNaoNulos")
    void updateCategory_deveAtualizarApenasCamposNaoNulos() {
        // Given
        UpdateCategoryParams params = new UpdateCategoryParams(null, name, null, null);
        when(getCategoryCase.execute(user, categoryId)).thenReturn(category);
        when(category.update(name, null, null, null)).thenReturn(true);
        // When
        Category result = updateCategoryCase.execute(user, categoryId, params);
        // Then
        assertNotNull(result);
        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }
}
