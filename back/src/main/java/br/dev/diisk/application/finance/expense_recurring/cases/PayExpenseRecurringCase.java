package br.dev.diisk.application.finance.expense_recurring.cases;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.expense_recurring.dtos.PayExpenseRecurringParams;
import br.dev.diisk.application.finance.expense_transaction.cases.AddExpenseTransactionCase;
import br.dev.diisk.application.finance.expense_transaction.dtos.AddExpenseTransactionParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.WishListItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayExpenseRecurringCase {

    private final GetExpenseRecurringCase getExpenseRecurringCase;
    private final AddExpenseTransactionCase addExpenseTransactionCase;
    private final UtilService utilService;

    @Transactional
    public ExpenseTransaction execute(User user, Long id, PayExpenseRecurringParams params) {

        if (params.getPaymentDate() == null)
            throw new NullPointerException("A data de pagamento é obrigatória");

        if (params.getReferenceDate() == null)
            throw new NullPointerException("A data de referência é obrigatória");

        if (id == null)
            throw new NullPointerException("O id da despesa recorrente é obrigatório");

        ExpenseRecurring expenseRecurring = getExpenseRecurringCase.execute(user, id);
        CreditCard creditCard = expenseRecurring.getCreditCard();
        WishListItem wishItem = expenseRecurring.getWishItem();
        LocalDateTime dueDate = null;

        if (expenseRecurring.getDueDay() != null)
            dueDate = params.getReferenceDate().withDayOfMonth(expenseRecurring.getDueDay().getValue());

        AddExpenseTransactionParams transactionParams = new AddExpenseTransactionParams();

        transactionParams.setCategoryId(expenseRecurring.getCategoryId());
        transactionParams.setCreditCardId(creditCard != null ? creditCard.getId() : null);
        transactionParams.setDescription(expenseRecurring.getDescription());
        transactionParams.setValue(expenseRecurring.getValue());
        transactionParams.setPaymentDate(params.getPaymentDate());
        transactionParams.setDueDate(dueDate);
        transactionParams.setRecurringReferenceDate(utilService.getFirstDayMonthReference(params.getReferenceDate()));
        transactionParams.setWishItemId(wishItem != null ? wishItem.getId() : null);
        transactionParams.setExpenseRecurringId(expenseRecurring.getId());

        ExpenseTransaction expenseTransaction = addExpenseTransactionCase.execute(user, transactionParams);

        return expenseTransaction;
    }
}
