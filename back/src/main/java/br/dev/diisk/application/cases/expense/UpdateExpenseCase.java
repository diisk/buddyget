package br.dev.diisk.application.cases.expense;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.dtos.expense.UpdateExpenseDto;
import br.dev.diisk.application.mappers.expense.UpdateExpenseDtoToExpenseMapper;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateExpenseCase {

    private final IExpenseRepository expenseRepository;
    private final GetExpenseCase getExpenseCase;
    private final UpdateExpenseDtoToExpenseMapper updateExpenseDtoToExpenseMapper;

    public Expense execute(User user, Long expenseId, UpdateExpenseDto dto) {

        Expense expense = getExpenseCase.execute(user, expenseId);

        updateExpenseDtoToExpenseMapper.update(user, dto, expense);

        validateIfCategoryIsValid(user, expense);

        expenseRepository.save(expense);

        return expense;
    }

    private void validateIfCategoryIsValid(User user, Expense expense) {
        if (expense.getCategory().getType() != CategoryTypeEnum.EXPENSE)
            throw new DbValueNotFoundException(getClass(), "categoryId");

    }

}
