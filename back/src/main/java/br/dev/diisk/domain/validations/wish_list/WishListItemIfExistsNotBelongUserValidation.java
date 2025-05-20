package br.dev.diisk.domain.validations.wish_list;

import br.dev.diisk.domain.entities.wish_list.WishListItem;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;

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
                throw new BadRequestValueCustomRuntimeException(classObj, "Wishlist item must belong to the same user",
                        null);

    }

}
