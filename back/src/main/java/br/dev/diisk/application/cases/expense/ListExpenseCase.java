package br.dev.diisk.application.cases.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.expense.ListExpenseFilter;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListExpenseCase {

    private final IExpenseRepository expenseRepository;

    public Page<Expense> execute(User user, ListExpenseFilter filter, Pageable pageable) {

        return expenseRepository.findBy(user.getId(), filter, pageable);
    }

}
