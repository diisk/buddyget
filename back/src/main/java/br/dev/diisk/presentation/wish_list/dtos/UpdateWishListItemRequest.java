package br.dev.diisk.presentation.wish_list.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.wish_list.WishItemPriorityEnum;

public record UpdateWishListItemRequest(
    String name,
    BigDecimal estimatedValue,
    WishItemPriorityEnum priority,
    String storeOrBrand,
    String link,
    String observation
) {}
