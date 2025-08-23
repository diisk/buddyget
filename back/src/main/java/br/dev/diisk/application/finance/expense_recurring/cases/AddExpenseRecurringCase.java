package br.dev.diisk.application.finance.expense_recurring.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.category.cases.GetCategoryCase;
import br.dev.diisk.application.credit_card.cases.GetCreditCardCase;
import br.dev.diisk.application.finance.expense_recurring.dtos.AddExpenseRecurringParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.application.wish_list.cases.GetWishListItemCase;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.WishListItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddExpenseRecurringCase {

    private final IExpenseRecurringRepository expenseRecurringRepository;
    private final GetCategoryCase getCategoryCase;
    private final GetWishListItemCase getWishListItemCase;
    private final GetCreditCardCase getCreditCardCase;
    private final UtilService utilService;

    @Transactional
    public ExpenseRecurring execute(User user, AddExpenseRecurringParams params) {

        String description = params.getDescription();
        Long categoryId = params.getCategoryId();
        Long wishItemId = params.getWishItemId();
        Long creditCardId = params.getCreditCardId();
        Integer dueDay = params.getDueDay();
        BigDecimal value = params.getValue();
        LocalDateTime startDate = params.getStartDate();
        LocalDateTime endDate = params.getEndDate() != null ? utilService.getLastDayMonthReference(params.getEndDate())
                : null;
        Category category = null;
        if (categoryId != null) {
            category = getCategoryCase.execute(user, categoryId);
        }

        ExpenseRecurring expenseRecurring = new ExpenseRecurring(description,
                new DataRange(startDate, endDate), category,
                value, user);

        if (wishItemId != null) {
            WishListItem wishItem = getWishListItemCase.execute(user, wishItemId);
            expenseRecurring.addWishItem(wishItem);
        }

        if (dueDay != null)
            expenseRecurring.setDueDay(new DayOfMonth(dueDay));

        if (creditCardId != null) {
            CreditCard creditCard = getCreditCardCase.execute(user, creditCardId);
            expenseRecurring.addCreditCard(creditCard);

            if (creditCard.getBillDueDay() != null)
                expenseRecurring.setDueDay(creditCard.getBillDueDay());

        }

        expenseRecurringRepository.save(expenseRecurring);

        return expenseRecurring;

    }
}
