package br.dev.diisk.presentation.transaction.expense.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.transaction.expense.ExpenseTransaction;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.credit_card.dtos.CreditCardResponse;
import br.dev.diisk.presentation.goal.dtos.GoalResponse;
import br.dev.diisk.presentation.wish_list.dtos.WishListItemResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpenseResponse {
        private Long id;
        private String dueDate;
        private ExpenseRecurringResponse recurringResponse;
        private CreditCardResponse creditCard;
        private WishListItemResponse wishitem;
        private GoalResponse goal;
        private String paymentDate;
        private BigDecimal value;
        private String description;
        private CategoryResponse category;
        private String status;
        private String createdAt;

        public ExpenseResponse(ExpenseTransaction expenseTransaction) {
                if (expenseTransaction == null)
                        return;
                this.id = expenseTransaction.getId();
                this.dueDate = expenseTransaction.getDueDate() != null ? expenseTransaction.getDueDate().toString()
                                : null;
                if (expenseTransaction.getExpenseRecurring() != null) {
                        this.recurringResponse = new ExpenseRecurringResponse(expenseTransaction.getExpenseRecurring());
                }
                if (expenseTransaction.getCreditCard() != null) {
                        this.creditCard = new CreditCardResponse(expenseTransaction.getCreditCard(),
                                        expenseTransaction.getPaymentDate());
                }
                if (expenseTransaction.getWishItem() != null) {
                        this.wishitem = new WishListItemResponse(expenseTransaction.getWishItem());
                }
                if (expenseTransaction.getGoal() != null) {
                        this.goal = new GoalResponse(expenseTransaction.getGoal());
                }
                this.paymentDate = expenseTransaction.getPaymentDate() != null
                                ? expenseTransaction.getPaymentDate().toString()
                                : null;
                this.value = expenseTransaction.getValue();
                this.description = expenseTransaction.getDescription();
                if (expenseTransaction.getCategory() != null) {
                        this.category = new CategoryResponse(expenseTransaction.getCategory());
                }
                this.status = "Pago";
                if (expenseTransaction.getPaymentDate() == null) {
                        this.status = expenseTransaction.getDueDate() != null
                                        && expenseTransaction.getDueDate().isBefore(LocalDateTime.now()) ? "Atrasado"
                                                        : "Pendente";
                }
                this.createdAt = expenseTransaction.getCreatedAt() != null
                                ? expenseTransaction.getCreatedAt().toString()
                                : null;
        }
}
