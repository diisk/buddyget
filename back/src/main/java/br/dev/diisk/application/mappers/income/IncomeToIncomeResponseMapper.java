package br.dev.diisk.application.mappers.income;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import br.dev.diisk.application.mappers.BaseMapper;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.presentation.dtos_OLDS.income.IncomeResponse;

@Component
public class IncomeToIncomeResponseMapper extends BaseMapper<Income, IncomeResponse> {

    public IncomeToIncomeResponseMapper(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected void doComplexMap(User user, Income source, IncomeResponse target) {
        target.setCategoryName(source.getCategory().getDescription());
        target.setCategoryId(source.getCategory().getId());
    }

}
