package br.dev.diisk.application.finance.expense_transaction.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetExpenseTransactionCase {

    private final IExpenseTransactionRepository expenseRepository;

    @Transactional
    public ExpenseTransaction execute(User user, Long expenseTransactionId) {
        ExpenseTransaction expenseTransaction = expenseRepository.findById(expenseTransactionId).orElse(null);
        if (expenseTransaction == null || expenseTransaction.getUserId() != user.getId())
            throw new DatabaseValueNotFoundException(getClass(), expenseTransactionId.toString());

        return expenseTransaction;

    }
}
