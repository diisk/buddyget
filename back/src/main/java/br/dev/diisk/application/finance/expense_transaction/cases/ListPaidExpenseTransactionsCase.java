package br.dev.diisk.application.finance.expense_transaction.cases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.finance.expense_transaction.ListPaidExpenseTransactionsFilter;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;
import br.dev.diisk.domain.shared.validations.DateLesserOrEqualNowValidation;
import br.dev.diisk.domain.shared.validations.StartDateHigherOrEqualEndDateValidation;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListPaidExpenseTransactionsCase {

    private final IExpenseTransactionRepository expenseRepository;

    @Transactional
    public Page<ExpenseTransaction> execute(User user, ListPaidExpenseTransactionsFilter filter, Pageable pageable) {

        if (filter.getEndDate() == null)
            filter.setEndDate(LocalDateTime.now());

        if (filter.getStartDate() == null) {
            LocalDateTime endDate = filter.getEndDate();
            LocalDateTime startDate = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), 1, 0, 0);
            filter.setStartDate(startDate);
        }

        List<IValidationStrategy> validations = List.of(
                new DateLesserOrEqualNowValidation(filter.getStartDate(), "startDate"),
                new StartDateHigherOrEqualEndDateValidation(filter.getStartDate(), filter.getEndDate()));

        validations.forEach(validation -> validation.validate(getClass()));

        return expenseRepository.findAllPaidBy(user.getId(), filter, pageable);
    }
}
