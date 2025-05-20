package br.dev.diisk.presentation.dtos.wishlist;

import java.math.BigDecimal;

import br.dev.diisk.domain.enums.wish_list.WishItemPriorityEnum;
import br.dev.diisk.presentation.dtos.transaction.ExpenseRecurringResponse;
import br.dev.diisk.presentation.dtos.transaction.ExpenseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WishListItemResponse {
    private Long id;
    private String name;
    private BigDecimal estimatedValue;
    private WishItemPriorityEnum priority;
    private String storeOrBrand;
    private String link;
    private String observation;
    private Long categoryId;
    private ExpenseResponse expense;
    private ExpenseRecurringResponse expenseRecurring;
    private Boolean purchased;
}
