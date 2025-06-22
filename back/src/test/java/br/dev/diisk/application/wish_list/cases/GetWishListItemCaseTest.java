package br.dev.diisk.application.wish_list.cases;

import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.IWishListItemRepository;
import br.dev.diisk.domain.wish_list.WishListItem;
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
 * Testes unitários para o caso de uso GetWishListItemCase.
 */
@ExtendWith(MockitoExtension.class)
class GetWishListItemCaseTest {

    @Mock
    private IWishListItemRepository wishListItemRepository;

    @InjectMocks
    private GetWishListItemCase getWishListItemCase;

    private User user;
    private WishListItem wishListItem;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        wishListItem = mock(WishListItem.class);
    }

    @Test
    @DisplayName("getWishListItem_deveRetornarItem_quandoPertenceAoUsuario")
    void getWishListItem_deveRetornarItem_quandoPertenceAoUsuario() {
        // Dado que o item existe e pertence ao usuário
        Long wishItemId = 1L;
        when(wishListItemRepository.findById(wishItemId)).thenReturn(Optional.of(wishListItem));
        when(wishListItem.getUserId()).thenReturn(10L);
        when(user.getId()).thenReturn(10L);

        // Quando executar o caso de uso
        WishListItem result = getWishListItemCase.execute(user, wishItemId);

        // Então deve retornar o item correto
        assertEquals(wishListItem, result);
        verify(wishListItemRepository).findById(wishItemId);
    }

    @Test
    @DisplayName("getWishListItem_deveLancarExcecao_quandoItemNaoExiste")
    void getWishListItem_deveLancarExcecao_quandoItemNaoExiste() {
        // Dado que o item não existe
        Long wishItemId = 2L;
        when(wishListItemRepository.findById(wishItemId)).thenReturn(Optional.empty());

        // Quando executar o caso de uso, deve lançar exceção
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getWishListItemCase.execute(user, wishItemId)
        );
        // Então deve validar o details da exceção
        assertNotNull(ex.getDetails());
        assertTrue(ex.getDetails().values().contains(wishItemId.toString()));
    }

    @Test
    @DisplayName("getWishListItem_deveLancarExcecao_quandoItemNaoPertenceAoUsuario")
    void getWishListItem_deveLancarExcecao_quandoItemNaoPertenceAoUsuario() {
        // Dado que o item existe mas pertence a outro usuário
        Long wishItemId = 3L;
        when(wishListItemRepository.findById(wishItemId)).thenReturn(Optional.of(wishListItem));
        when(wishListItem.getUserId()).thenReturn(20L);
        when(user.getId()).thenReturn(10L);

        // Quando executar o caso de uso, deve lançar exceção
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getWishListItemCase.execute(user, wishItemId)
        );
        // Então deve validar o details da exceção
        assertNotNull(ex.getDetails());
        assertTrue(ex.getDetails().values().contains(wishItemId.toString()));
    }
}
