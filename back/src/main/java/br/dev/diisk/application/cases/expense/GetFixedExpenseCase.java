package br.dev.diisk.application.cases.expense;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetFixedExpenseCase {

    private final IFixedExpenseRepository fixedExpenseRepository;

    public FixedExpense execute(User user, Long id) {
        FixedExpense fixedExpense = fixedExpenseRepository.findById(id).orElse(null);
        if (fixedExpense == null || fixedExpense.getUser().getId() != user.getId())
            throw new DbValueNotFoundException(getClass(), "id");

        return fixedExpense;
    }

}
