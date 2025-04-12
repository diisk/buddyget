package br.dev.diisk.application.mappers.expense;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.cases.category.GetCategoryCase;
import br.dev.diisk.application.cases.credit_card.GetCreditCardCase;
import br.dev.diisk.application.cases.expense.GetFixedExpenseCase;
import br.dev.diisk.application.dtos.expense.AddExpenseDto;
import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;

@Component
public class AddExpenseDtoToExpenseMapper extends BaseMapper<AddExpenseDto, Expense> {

    private final GetCategoryCase getCategoryCase;
    private final GetCreditCardCase getCreditCardCase;
    private final GetFixedExpenseCase getFixedExpenseCase;

    public AddExpenseDtoToExpenseMapper(ModelMapper mapper, GetCategoryCase getCategoryCase,
            GetCreditCardCase getCreditCardCase, GetFixedExpenseCase getFixedExpenseCase) {
        super(mapper);
        this.getCategoryCase = getCategoryCase;
        this.getCreditCardCase = getCreditCardCase;
        this.getFixedExpenseCase = getFixedExpenseCase;
    }

    @Override
    protected void doComplexMap(User user, AddExpenseDto source, Expense target) {
        Category category = getCategoryCase.execute(user, source.getCategoryId());
        target.setCategory(category);

        if (source.getCreditCardId() != null) {
            CreditCard creditCard = getCreditCardCase.execute(user, source.getCreditCardId());
            target.setCreditCard(creditCard);
        }

        if (source.getFixedExpenseId() != null) {
            FixedExpense fixedExpense = getFixedExpenseCase.execute(user, source.getFixedExpenseId());
            target.setFixedExpense(fixedExpense);
        }
    }

}
