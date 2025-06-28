package br.dev.diisk.application.category.cases;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.ICategoryRepository;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryCaseTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private DeleteCategoryCase deleteCategoryCase;

    @Test
    void deleteCategoryCase_deveMarcarCategoriaComoExcluida_quandoCategoriaExistir() {
        // Given - Preparar uma categoria existente
        Long categoryId = 1L;
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        // When - Executar a deleção
        deleteCategoryCase.execute(user, categoryId);

        // Then - Verificar se a categoria foi marcada como deletada e salva
        assertTrue(category.isDeleted(), "A categoria deve estar marcada como deletada");
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void deleteCategoryCase_naoDeveFazerNada_quandoCategoriaNaoExistir() {
        // Given - Categoria não existe no repositório
        Long categoryId = 999L;
        User user = UserFixture.umUsuarioComId(1L);
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When - Tentar deletar categoria inexistente
        deleteCategoryCase.execute(user, categoryId);

        // Then - Verificar que nenhuma operação de salvamento foi executada
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteCategoryCase_naoDeveFazerNada_quandoCategoriaForNull() {
        // Given - Categoria retornada é null
        Long categoryId = 2L;
        User user = UserFixture.umUsuarioComId(1L);
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When - Executar com categoria null
        deleteCategoryCase.execute(user, categoryId);

        // Then - Verificar que não houve tentativa de salvamento
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteCategoryCase_deveMarcarCategoriaComoExcluida_quandoCategoriaDeRenda() {
        // Given - Preparar uma categoria de renda existente
        Long categoryId = 3L;
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.INCOME, user);
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        // When - Executar a deleção
        deleteCategoryCase.execute(user, categoryId);

        // Then - Verificar se a categoria foi marcada como deletada e salva
        assertTrue(category.isDeleted(), "A categoria deve estar marcada como deletada");
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(category);
    }
}
