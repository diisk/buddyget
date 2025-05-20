package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import br.dev.diisk.presentation.dtos.creditcard.CreditCardResponse;
import br.dev.diisk.presentation.dtos.wishlist.WishListItemResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRecurringResponse {
    private Long id;
    private Integer dueDay;
    private InstallmentResponse installmentPlan;
    private CreditCardResponse creditCard;
    private WishListItemResponse wishitem;
    private BigDecimal value;
    private String description;
    private CategoryResponse category;
    private String startDate;
    private String endDate;
    private Integer recurringDay;
}
