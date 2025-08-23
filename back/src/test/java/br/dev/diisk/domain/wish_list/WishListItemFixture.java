package br.dev.diisk.domain.wish_list;

import java.math.BigDecimal;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.user.User;

public class WishListItemFixture {
    public static WishListItem umWishListItemComId(Long id, User user, Category category) {
        WishListItem wishListItem = new WishListItem(
                user,
                "Loja Teste",
                new BigDecimal("150.00"),
                category);
        wishListItem.setId(id);
        return wishListItem;
    }
}
