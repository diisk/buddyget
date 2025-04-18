package br.dev.diisk.application.mappers.expense;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.cases.category.GetCategoryCase;
import br.dev.diisk.application.cases.credit_card.GetCreditCardCase;
import br.dev.diisk.application.dtos.expense.UpdateExpenseDto;
import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;

@Component
public class UpdateExpenseDtoToExpenseMapper extends BaseMapper<UpdateExpenseDto, Expense> {

    private final GetCategoryCase getCategoryCase;
    private final GetCreditCardCase getCreditCardCase;

    public UpdateExpenseDtoToExpenseMapper(ModelMapper mapper, GetCategoryCase getCategoryCase,
            GetCreditCardCase getCreditCardCase) {
        super(mapper);
        this.getCategoryCase = getCategoryCase;
        this.getCreditCardCase = getCreditCardCase;
    }

    @Override
    protected void doComplexUpdate(User user, UpdateExpenseDto source, Expense target) {
        if (source.getCategoryId() != null) {
            Category category = getCategoryCase.execute(user, source.getCategoryId());
            target.setCategory(category);
        }
        if (source.getCreditCardId() != null) {
            Long creditCardId = source.getCreditCardId();
            CreditCard creditCard = creditCardId == -1 ? null : getCreditCardCase.execute(user, creditCardId);
            target.setCreditCard(creditCard);
        }
    }

}
