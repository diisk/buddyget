package br.dev.diisk.presentation.dtos.wishlist;

import java.math.BigDecimal;

import br.dev.diisk.domain.enums.wish_list.WishItemPriorityEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateWishListItemRequest {
    private String name;
    private BigDecimal estimatedValue;
    private WishItemPriorityEnum priority;
    private String storeOrBrand;
    private String link;
    private String observation;
    private Long categoryId;
}
