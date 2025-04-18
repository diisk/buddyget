package br.dev.diisk.application.cases.expense;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteExpenseCase {

    private final IExpenseRepository expenseRepository;

    public void execute(User user, Long id) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null && expense.getUser().getId() == user.getId())
            expenseRepository.delete(expense);
    }

}
