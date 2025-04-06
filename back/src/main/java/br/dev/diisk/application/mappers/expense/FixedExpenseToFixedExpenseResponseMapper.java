package br.dev.diisk.application.mappers.expense;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.infra.services.MessageService;
import br.dev.diisk.presentation.dtos.expense.FixedExpenseResponse;

@Component
public class FixedExpenseToFixedExpenseResponseMapper extends BaseMapper<FixedExpense, FixedExpenseResponse> {

    private final MessageService messageService;

    public FixedExpenseToFixedExpenseResponseMapper(ModelMapper mapper, MessageService messageService) {
        super(mapper);
        this.messageService = messageService;
    }

    @Override
    protected void doComplexMap(User user, FixedExpense source, FixedExpenseResponse target) {
        target.setCategoryId(source.getCategory().getId());
        target.setCategoryName(source.getCategory().getDescription());
        target.setCreditCardId(source.getCreditCard().getId());
        target.setTypeName(messageService.getMessage(source.getType().getTitlePath()));
    }

}
