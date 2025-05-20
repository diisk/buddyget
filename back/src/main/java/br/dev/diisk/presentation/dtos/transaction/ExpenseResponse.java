package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import br.dev.diisk.presentation.dtos.creditcard.CreditCardResponse;
import br.dev.diisk.presentation.dtos.goal.GoalResponse;
import br.dev.diisk.presentation.dtos.wishlist.WishListItemResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

    private Long id;
    private String dueDate;
    private ExpenseRecurringResponse recurringResponse;
    private CreditCardResponse creditCard;
    private WishListItemResponse wishitem;
    private GoalResponse goal;
    private String date;
    private BigDecimal value;
    private String description;
    private CategoryResponse category;
    private String status;//Pendente, Pago, Atrasado - Baseado na data de pagamento e data de vencimento
    private String createdAt;

}
