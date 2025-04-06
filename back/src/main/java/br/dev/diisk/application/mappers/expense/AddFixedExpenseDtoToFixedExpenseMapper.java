package br.dev.diisk.application.mappers.expense;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.cases.category.GetCategoryCase;
import br.dev.diisk.application.cases.credit_card.GetCreditCardCase;
import br.dev.diisk.application.dtos.expense.AddFixedExpenseDto;
import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;

@Component
public class AddFixedExpenseDtoToFixedExpenseMapper extends BaseMapper<AddFixedExpenseDto, FixedExpense> {

    private final GetCategoryCase getCategoryCase;
    private final GetCreditCardCase getCreditCardCase;

    public AddFixedExpenseDtoToFixedExpenseMapper(ModelMapper mapper, GetCategoryCase getCategoryCase,
            GetCreditCardCase getCreditCardCase) {
        super(mapper);
        this.getCategoryCase = getCategoryCase;
        this.getCreditCardCase = getCreditCardCase;
    }

    @Override
    protected void doComplexMap(User user, AddFixedExpenseDto source, FixedExpense target) {
        Category category = getCategoryCase.execute(user, source.getCategoryId());
        CreditCard creditCard = getCreditCardCase.execute(user, source.getCreditCardId());
        target.setCategory(category);
        target.setCreditCard(creditCard);
    }

}
