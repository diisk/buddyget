package br.dev.diisk.domain.validations.wish_list;

import br.dev.diisk.domain.entities.wish_list.WishListItem;
import br.dev.diisk.domain.exceptions.BusinessException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
import java.util.Map;

public class WishListItemIfExistsNotBelongUserValidation implements IValidationStrategy {

    private final WishListItem wishListItem;
    private final Long userId;

    public WishListItemIfExistsNotBelongUserValidation(WishListItem wishListItem, Long userId) {
        this.wishListItem = wishListItem;
        this.userId = userId;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (wishListItem != null)
            if (wishListItem.getUserId() != userId)
                throw new BusinessException(classObj, "O item da lista de desejos deve pertencer ao mesmo usuário",
                        Map.of("wishListItemId", wishListItem.getId().toString()));
    }
}
