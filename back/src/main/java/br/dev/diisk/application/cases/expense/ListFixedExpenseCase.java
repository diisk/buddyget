package br.dev.diisk.application.cases.expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.expense.ListFixedExpenseFilter;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListFixedExpenseCase {

    private final IFixedExpenseRepository fixedExpenseRepository;

    public Page<FixedExpense> execute(User user, ListFixedExpenseFilter filter, Pageable pageable) {

        return fixedExpenseRepository.findBy(user.getId(), filter, pageable);
    }

}
