package br.dev.diisk.application.finance.expense_recurring.cases;

import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.AdjustRecurringCase;
import br.dev.diisk.domain.finance.Transaction;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.infra.finance.expense_recurring.ExpenseRecurringRepository;
import br.dev.diisk.infra.finance.expense_transaction.ExpenseTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdjustExpenseRecurringCase {

    private final AdjustRecurringCase adjustRecurringCase;
    private final ExpenseTransactionRepository expenseTransactionRepository;
    private final ExpenseRecurringRepository expenseRecurringRepository;

    @Transactional
    public void execute(ExpenseRecurring expenseRecurring) {
        List<Transaction> relatedTransactions = expenseTransactionRepository
                .findAllRecurringRelatedBy(List.of(expenseRecurring.getId())).stream().map(it -> (Transaction) it)
                .toList();
        adjustRecurringCase.execute(expenseRecurring, relatedTransactions, er -> {
            expenseRecurringRepository.save((ExpenseRecurring) er);
            return null;
        });
    }
}
