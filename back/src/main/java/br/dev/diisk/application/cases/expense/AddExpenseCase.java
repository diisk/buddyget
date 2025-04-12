package br.dev.diisk.application.cases.expense;

import org.springframework.stereotype.Service;
import br.dev.diisk.application.dtos.expense.AddExpenseDto;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.mappers.expense.AddExpenseDtoToExpenseMapper;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.expense.IExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddExpenseCase {

    private final IExpenseRepository expenseRepository;
    private final AddExpenseDtoToExpenseMapper addExpenseDtoToExpenseMapper;

    public Expense execute(User user, AddExpenseDto dto) {

        Expense expense = addExpenseDtoToExpenseMapper.apply(user, dto);

        validateIfCategoryIsValid(user, expense);

        expense.setUser(user);

        expenseRepository.save(expense);

        return expense;
    }

    private void validateIfCategoryIsValid(User user, Expense expense) {
        if (expense.getCategory().getType() != CategoryTypeEnum.EXPENSE)
            throw new DbValueNotFoundException(getClass(), "categoryId");

    }

}
