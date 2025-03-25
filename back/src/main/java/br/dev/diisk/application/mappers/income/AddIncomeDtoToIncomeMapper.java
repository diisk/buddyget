package br.dev.diisk.application.mappers.income;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.cases.category.GetCategoryCase;
import br.dev.diisk.application.dtos.income.AddIncomeDto;
import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;

@Component
public class AddIncomeDtoToIncomeMapper extends BaseMapper<AddIncomeDto, Income> {

    private final GetCategoryCase getCategoryCase;

    public AddIncomeDtoToIncomeMapper(ModelMapper mapper, GetCategoryCase getCategoryCase) {
        super(mapper);
        this.getCategoryCase = getCategoryCase;
    }

    @Override
    protected void doComplexMap(User user, AddIncomeDto source, Income target) {
        Category category = getCategoryCase.execute(user, source.getCategoryId());
        target.setCategory(category);
    }

}
