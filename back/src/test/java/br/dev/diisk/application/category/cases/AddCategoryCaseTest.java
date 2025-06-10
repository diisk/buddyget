package br.dev.diisk.application.category.cases;

import br.dev.diisk.application.category.dtos.AddCategoryParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.ICategoryRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * Testes unitários para o caso de uso AddCategoryCase.
 */
@ExtendWith(MockitoExtension.class)
class AddCategoryCaseTest {
    @Mock
    private ICategoryRepository categoryRepository;
    @Mock
    private User user;
    @InjectMocks
    private AddCategoryCase addCategoryCase;

    private AddCategoryParams buildParams(String name, String description, String iconName, String color, CategoryTypeEnum type) {
        return new AddCategoryParams(description, name, type, color, iconName);
    }

    @Test
    @DisplayName("Deve adicionar categoria corretamente quando dados válidos são fornecidos")
    void addCategory_deveAdicionarCorretamente_quandoDadosValidos() {
        // Given
        AddCategoryParams params = buildParams("Alimentação", "Gastos com comida", "food", "#FFAA00", CategoryTypeEnum.EXPENSE);
        Mockito.when(categoryRepository.findBy(anyLong(), any(), anyString())).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Category result = addCategoryCase.execute(user, params);

        // Then
        assertNotNull(result);
        assertEquals("Alimentação", result.getName());
        assertEquals("Gastos com comida", result.getDescription());
        assertEquals("food", result.getIconName());
        assertEquals(CategoryTypeEnum.EXPENSE, result.getType());
        assertEquals("#FFAA00", result.getColor().getValue());
        Mockito.verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Deve lançar NullOrEmptyException quando o nome for nulo ou vazio")
    void addCategory_deveLancarExcecao_quandoNomeNuloOuVazio() {
        // Cenário 1: nome nulo
        final AddCategoryParams paramsNulo = buildParams(null, "desc", "icon", "#FFFFFF", CategoryTypeEnum.EXPENSE);
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> addCategoryCase.execute(user, paramsNulo));
        assertEquals("name", ex.getDetails().get("campo"));

        // Cenário 2: nome vazio
        final AddCategoryParams paramsVazio = buildParams("   ", "desc", "icon", "#FFFFFF", CategoryTypeEnum.EXPENSE);
        ex = assertThrows(NullOrEmptyException.class, () -> addCategoryCase.execute(user, paramsVazio));
        assertEquals("name", ex.getDetails().get("campo"));
    }

    @Test
    @DisplayName("Deve lançar NullOrEmptyException quando o tipo for nulo")
    void addCategory_deveLancarExcecao_quandoTipoNulo() {
        // Given
        AddCategoryParams params = buildParams("Nome", "desc", "icon", "#FFFFFF", null);
        // When / Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> addCategoryCase.execute(user, params));
        assertEquals("type", ex.getDetails().get("campo"));
    }

    @Test
    @DisplayName("Deve lançar DatabaseValueConflictException quando já existir categoria com mesmo nome e tipo para o usuário")
    void addCategory_deveLancarExcecao_quandoCategoriaJaExistente() {
        // Given
        AddCategoryParams params = buildParams("Alimentação", "desc", "icon", "#FFFFFF", CategoryTypeEnum.EXPENSE);
        Mockito.when(categoryRepository.findBy(anyLong(), any(), anyString())).thenReturn(Optional.of(Mockito.mock(Category.class)));
        // When / Then
        DatabaseValueConflictException ex = assertThrows(DatabaseValueConflictException.class, () -> addCategoryCase.execute(user, params));
        assertEquals("Alimentação", ex.getDetails().get("name"));
        assertEquals(CategoryTypeEnum.EXPENSE.name(), ex.getDetails().get("type"));
    }

    @Test
    @DisplayName("Deve lançar NullOrEmptyException quando descrição ou ícone forem nulos")
    void addCategory_deveLancarExcecao_quandoDescricaoOuIconeNulos() {
        // Cenário: descrição nula
        final AddCategoryParams paramsDescricaoNula = buildParams("Viagem", null, "icon", "#123ABC", CategoryTypeEnum.EXPENSE);
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> addCategoryCase.execute(user, paramsDescricaoNula));
        assertEquals("description", ex.getDetails().get("campo"));

        // Cenário: ícone nulo
        final AddCategoryParams paramsIconeNulo = buildParams("Viagem", "desc", null, "#123ABC", CategoryTypeEnum.EXPENSE);
        ex = assertThrows(NullOrEmptyException.class, () -> addCategoryCase.execute(user, paramsIconeNulo));
        assertEquals("iconName", ex.getDetails().get("campo"));
    }

}
