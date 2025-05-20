package br.dev.diisk.application.cases.expense;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetExpenseCase {

    private final IExpenseRepository expenseRepository;

    public Expense execute(User user, Long expenseId) {

        Expense expense = expenseRepository.findById(expenseId).orElse(null);
        if (expense == null || expense.getUser().getId() != user.getId())
            throw new DbValueNotFoundException(getClass(), "id");

        return expense;
    }

}
