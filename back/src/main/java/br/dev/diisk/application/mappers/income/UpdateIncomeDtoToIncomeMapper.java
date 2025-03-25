package br.dev.diisk.application.mappers.income;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.cases.category.GetCategoryCase;
import br.dev.diisk.application.dtos.income.UpdateIncomeDto;
import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;

@Component
public class UpdateIncomeDtoToIncomeMapper extends BaseMapper<UpdateIncomeDto, Income> {

    private final GetCategoryCase getCategoryCase;

    public UpdateIncomeDtoToIncomeMapper(ModelMapper mapper, GetCategoryCase getCategoryCase) {
        super(mapper);
        this.getCategoryCase = getCategoryCase;
    }

    @Override
    protected void doComplexUpdate(User user, UpdateIncomeDto source, Income target) {
        if (source.getCategoryId() != null) {
            Category category = getCategoryCase.execute(user, source.getCategoryId());
            target.setCategory(category);
        }

    }

}
