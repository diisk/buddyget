package br.dev.diisk.application.mappers.expense;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.presentation.dtos_OLDS.expense.ExpenseResponse;

@Component
public class ExpenseToExpenseResponseMapper extends BaseMapper<Expense, ExpenseResponse> {

    public ExpenseToExpenseResponseMapper(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected void doComplexMap(User user, Expense source, ExpenseResponse target) {
        target.setCategoryId(source.getCategory().getId());
        target.setCategoryName(source.getCategory().getDescription());

        if (source.getWishItem() != null)
            target.setWishItemId(source.getWishItem().getId());

        if (source.getCreditCard() != null)
            target.setCreditCardId(source.getCreditCard().getId());
        if (source.getFixedExpense() != null)
            target.setFixedExpenseId(source.getFixedExpense().getId());
    }

}
