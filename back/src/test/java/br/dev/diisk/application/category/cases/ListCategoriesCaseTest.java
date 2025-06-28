package br.dev.diisk.application.category.cases;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.ICategoryRepository;
import br.dev.diisk.domain.category.ListCategoriesFilter;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso ListCategoriesCase.
 * Segue o padrão Given-When-Then e cobre cenários felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class ListCategoriesCaseTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private ListCategoriesCase listCategoriesCase;

    /**
     * Deve retornar página de categorias corretamente quando existem categorias para o usuário.
     */
    @Test
    @DisplayName("listCategoriesCase_deveRetornarPaginaDeCategorias_quandoExistemCategorias")
    void listCategoriesCase_deveRetornarPaginaDeCategorias_quandoExistemCategorias() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        ListCategoriesFilter filter = new ListCategoriesFilter("categoria", CategoryTypeEnum.EXPENSE);
        Pageable pageable = PageRequest.of(0, 10);
        
        Category categoria1 = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        Category categoria2 = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.EXPENSE, user);
        List<Category> categorias = Arrays.asList(categoria1, categoria2);
        Page<Category> expectedPage = new PageImpl<>(categorias, pageable, 2);
        
        when(categoryRepository.findAllBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When
        Page<Category> result = listCategoriesCase.execute(user, filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(categoria1, result.getContent().get(0));
        assertEquals(categoria2, result.getContent().get(1));
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        verify(categoryRepository).findAllBy(user.getId(), filter, pageable);
    }

    /**
     * Deve retornar página vazia quando não existem categorias para o usuário.
     */
    @Test
    @DisplayName("listCategoriesCase_deveRetornarPaginaVazia_quandoNaoExistemCategorias")
    void listCategoriesCase_deveRetornarPaginaVazia_quandoNaoExistemCategorias() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        ListCategoriesFilter filter = new ListCategoriesFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        when(categoryRepository.findAllBy(user.getId(), filter, pageable)).thenReturn(emptyPage);

        // When
        Page<Category> result = listCategoriesCase.execute(user, filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        verify(categoryRepository).findAllBy(user.getId(), filter, pageable);
    }

    /**
     * Deve retornar categorias corretamente quando filtro possui apenas searchString.
     */
    @Test
    @DisplayName("listCategoriesCase_deveRetornarCategorias_quandoFiltroComApenasSearchString")
    void listCategoriesCase_deveRetornarCategorias_quandoFiltroComApenasSearchString() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        ListCategoriesFilter filter = new ListCategoriesFilter("alimentacao", null);
        Pageable pageable = PageRequest.of(0, 5);
        
        Category categoria = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        List<Category> categorias = Arrays.asList(categoria);
        Page<Category> expectedPage = new PageImpl<>(categorias, pageable, 1);
        
        when(categoryRepository.findAllBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When
        Page<Category> result = listCategoriesCase.execute(user, filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(categoria, result.getContent().get(0));
        verify(categoryRepository).findAllBy(user.getId(), filter, pageable);
    }

    /**
     * Deve retornar categorias corretamente quando filtro possui apenas tipo.
     */
    @Test
    @DisplayName("listCategoriesCase_deveRetornarCategorias_quandoFiltroComApenasType")
    void listCategoriesCase_deveRetornarCategorias_quandoFiltroComApenasType() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        ListCategoriesFilter filter = new ListCategoriesFilter(null, CategoryTypeEnum.INCOME);
        Pageable pageable = PageRequest.of(1, 5);
        
        Category categoria = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        List<Category> categorias = Arrays.asList(categoria);
        Page<Category> expectedPage = new PageImpl<>(categorias, pageable, 6);
        
        when(categoryRepository.findAllBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When
        Page<Category> result = listCategoriesCase.execute(user, filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(6, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(categoria, result.getContent().get(0));
        assertEquals(1, result.getNumber());
        assertEquals(5, result.getSize());
        verify(categoryRepository).findAllBy(user.getId(), filter, pageable);
    }

    /**
     * Deve retornar categorias corretamente quando filtro está vazio.
     */
    @Test
    @DisplayName("listCategoriesCase_deveRetornarCategorias_quandoFiltroVazio")
    void listCategoriesCase_deveRetornarCategorias_quandoFiltroVazio() {
        // Given
        User user = UserFixture.umUsuarioComId(2L);
        ListCategoriesFilter filter = new ListCategoriesFilter();
        Pageable pageable = PageRequest.of(0, 20);
        
        Category categoria1 = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        Category categoria2 = CategoryFixture.umaCategoriaComId(2L, CategoryTypeEnum.INCOME, user);
        List<Category> categorias = Arrays.asList(categoria1, categoria2);
        Page<Category> expectedPage = new PageImpl<>(categorias, pageable, 2);
        
        when(categoryRepository.findAllBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When
        Page<Category> result = listCategoriesCase.execute(user, filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(categoria1, result.getContent().get(0));
        assertEquals(categoria2, result.getContent().get(1));
        verify(categoryRepository).findAllBy(user.getId(), filter, pageable);
    }

    /**
     * Deve lançar exceção quando usuário for nulo.
     */
    @Test
    @DisplayName("listCategoriesCase_deveLancarExcecao_quandoUsuarioForNulo")
    void listCategoriesCase_deveLancarExcecao_quandoUsuarioForNulo() {
        // Given
        ListCategoriesFilter filter = new ListCategoriesFilter();
        Pageable pageable = PageRequest.of(0, 10);

        // When / Then
        assertThrows(NullPointerException.class, () -> 
            listCategoriesCase.execute(null, filter, pageable)
        );
        verify(categoryRepository, never()).findAllBy(any(), any(), any());
    }

    /**
     * Deve funcionar corretamente quando filtro for nulo.
     */
    @Test
    @DisplayName("listCategoriesCase_deveFuncionar_quandoFiltroForNulo")
    void listCategoriesCase_deveFuncionar_quandoFiltroForNulo() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        when(categoryRepository.findAllBy(user.getId(), null, pageable)).thenReturn(emptyPage);

        // When
        Page<Category> result = listCategoriesCase.execute(user, null, pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(categoryRepository).findAllBy(user.getId(), null, pageable);
    }
}
