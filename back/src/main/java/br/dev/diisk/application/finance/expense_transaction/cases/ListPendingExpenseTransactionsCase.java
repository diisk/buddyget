package br.dev.diisk.application.finance.expense_transaction.cases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.ListRecurringReferenceDates;
import br.dev.diisk.application.finance.expense_transaction.dtos.PendingExpenseTransactionDTO;
import br.dev.diisk.domain.finance.TransactionStatusEnum;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListPendingExpenseTransactionsCase {

    private final IExpenseTransactionRepository expenseRepository;
    private final IExpenseRecurringRepository expenseRecurringRepository;
    private final ListRecurringReferenceDates listRecurringReferenceDates;

    @Transactional
    public List<PendingExpenseTransactionDTO> execute(User user) {
        List<PendingExpenseTransactionDTO> pendingTransactions = expenseRepository.findAllPendingBy(user.getId())
                .stream().map(PendingExpenseTransactionDTO::new).collect(Collectors.toList());
        List<ExpenseRecurring> activeRecurrings = expenseRecurringRepository.findAllActiveBy(user.getId());
        List<Long> expenseRecurringIds = activeRecurrings.stream()
                .map(ExpenseRecurring::getId)
                .toList();
        List<ExpenseTransaction> recurringRelatedTransactions = expenseRepository
                .findAllRecurringRelatedBy(expenseRecurringIds);

        for (ExpenseRecurring recurring : activeRecurrings) {
            List<ExpenseTransaction> relatedTransactions = recurringRelatedTransactions.stream()
                    .filter(transaction -> {
                        ExpenseRecurring transactionRecurring = transaction.getExpenseRecurring();
                        return transactionRecurring != null && transactionRecurring.getId().equals(recurring.getId());
                    }).toList();

            List<LocalDateTime> missingReferenceDates = listRecurringReferenceDates.execute(recurring).stream()
                    .filter(referenceDate -> {
                        return relatedTransactions.stream()
                                .noneMatch(transaction -> transaction.getRecurringReferenceDate()
                                        .isEqual(referenceDate));
                    }).toList();

            if (missingReferenceDates.size() == 0)
                continue;

            List<PendingExpenseTransactionDTO> newTransactions = missingReferenceDates.stream()
                    .map(referenceDate -> {
                        Integer dueDayValue = recurring.getDueDayValue();
                        String status = TransactionStatusEnum.PENDING.getDescription();
                        if (dueDayValue != null) {
                            LocalDateTime dueDate = referenceDate.withDayOfMonth(dueDayValue);
                            if (LocalDateTime.now().isAfter(dueDate))
                                status = TransactionStatusEnum.LATE.getDescription();

                        }
                        return new PendingExpenseTransactionDTO(recurring, referenceDate,
                                status);
                    })
                    .collect(Collectors.toList());
            pendingTransactions.addAll(newTransactions);
        }

        pendingTransactions.sort((a, b) -> {
            if (a.dueDate() == null && b.dueDate() == null)
                return 0;
            if (a.dueDate() == null)
                return 1;
            if (b.dueDate() == null)
                return -1;
            return a.dueDate().compareTo(b.dueDate());
        });
        return pendingTransactions;
    }
}
