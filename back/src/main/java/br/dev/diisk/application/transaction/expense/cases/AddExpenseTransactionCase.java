package br.dev.diisk.application.transaction.expense.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.credit_card.cases.GetCreditCardCase;
import br.dev.diisk.application.goal.cases.GetGoalCase;
import br.dev.diisk.application.monthly_summary.cases.AddMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.transaction.expense.dtos.AddExpenseTransactionParams;
import br.dev.diisk.application.wish_list.cases.GetWishListItemCase;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.transaction.expense.ExpenseRecurring;
import br.dev.diisk.domain.transaction.expense.ExpenseTransaction;
import br.dev.diisk.domain.transaction.expense.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.WishListItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddExpenseTransactionCase {

    private final IExpenseTransactionRepository expenseRepository;
    private final GetCategoryCase getCategoryCase;
    private final GetGoalCase getGoalCase;
    private final GetCreditCardCase getCreditCardCase;
    private final GetWishListItemCase getWishListItemCase;
    private final GetExpenseRecurringCase getExpenseRecurringCase;
    private final AddMonthlySummaryValueCase addMonthlySummaryValueCase;

    @Transactional
    public ExpenseTransaction execute(User user, AddExpenseTransactionParams params) {
        String description = params.getDescription();
        Long categoryId = params.getCategoryId();
        BigDecimal value = params.getValue();
        LocalDateTime paymentDate = params.getPaymentDate();
        LocalDateTime dueDate = params.getDueDate();
        Long wishItemId = params.getWishItemId();
        Long expenseRecurringId = params.getExpenseRecurringId();
        Long goalId = params.getGoalId();
        Long creditCardId = params.getCreditCardId();

        Category category = null;
        if (categoryId != null)
            category = getCategoryCase.execute(user, categoryId);

        CreditCard creditCard = null;
        if (creditCardId != null)
            creditCard = getCreditCardCase.execute(user, creditCardId);

        WishListItem wishItem = null;
        if (wishItemId != null)
            wishItem = getWishListItemCase.execute(user, wishItemId);

        ExpenseRecurring expenseRecurring = null;
        if (expenseRecurringId != null)
            expenseRecurring = getExpenseRecurringCase.execute(user, expenseRecurringId);

        Goal goal = null;
        if (goalId != null)
            goal = getGoalCase.execute(user, goalId);

        ExpenseTransaction expenseTransaction = new ExpenseTransaction(description, category, value, paymentDate, user);

        if (dueDate != null)
            expenseTransaction.addDueDate(dueDate);

        if (creditCard != null)
            expenseTransaction.addCreditCard(creditCard);

        if (expenseRecurring != null)
            expenseTransaction.addExpenseRecurring(expenseRecurring);

        if (wishItem != null)
            expenseTransaction.addWishItem(wishItem);

        if (goal != null)
            expenseTransaction.addGoal(goal);

        expenseRepository.save(expenseTransaction);

        if (paymentDate != null)
            addMonthlySummaryValueCase.execute(user,
                    new AddMonthlySummaryValueParams(paymentDate.getMonthValue(), paymentDate.getYear(), value,
                            category));

        return expenseTransaction;
    }
}
