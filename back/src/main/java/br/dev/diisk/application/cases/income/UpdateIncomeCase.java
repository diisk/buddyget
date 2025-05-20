package br.dev.diisk.application.cases.income;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.dtos.income.UpdateIncomeDto;
import br.dev.diisk.application.mappers.income.UpdateIncomeDtoToIncomeMapper;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateIncomeCase {

    private final UpdateIncomeDtoToIncomeMapper mapper;
    private final GetIncomeCase getIncomeCase;
    private final IIncomeRepository incomeRepository;

    public Income execute(User user, Long incomeId, UpdateIncomeDto dto) {

        Income income = getIncomeCase.execute(user, incomeId);
        mapper.update(user, dto, income);

        validateIfCategoryIsValid(user, income);

        return incomeRepository.save(income);
    }

    private void validateIfCategoryIsValid(User user, Income income) {
        if (income.getCategory().getType() != CategoryTypeEnum.INCOME)
            throw new DbValueNotFoundException(getClass(), "categoryId");

    }

}
