package br.dev.diisk.presentation.dtos.wishlist;

import java.math.BigDecimal;

import br.dev.diisk.domain.enums.wish_list.WishItemPriorityEnum;

public record UpdateWishListItemRequest(
    String name,
    BigDecimal estimatedValue,
    WishItemPriorityEnum priority,
    String storeOrBrand,
    String link,
    String observation
) {}
