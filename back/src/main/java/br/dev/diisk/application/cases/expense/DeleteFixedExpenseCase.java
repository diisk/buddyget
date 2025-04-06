package br.dev.diisk.application.cases.expense;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteFixedExpenseCase {

    private final IFixedExpenseRepository fixedExpenseRepository;

    public void execute(User user, Long id) {
        FixedExpense fixedExpense = fixedExpenseRepository.findById(id).orElse(null);
        if (fixedExpense != null && fixedExpense.getUser().getId() == user.getId())
            fixedExpenseRepository.delete(fixedExpense);
    }

}
