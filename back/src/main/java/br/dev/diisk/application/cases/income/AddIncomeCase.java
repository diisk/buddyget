package br.dev.diisk.application.cases.income;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.dtos.income.AddIncomeDto;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.application.mappers.income.AddIncomeDtoToIncomeMapper;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddIncomeCase {

    private final AddIncomeDtoToIncomeMapper mapper;
    private final IIncomeRepository incomeRepository;

    public Income execute(User user, AddIncomeDto dto) {

        Income income = mapper.apply(user, dto);

        validateIfCategoryIsValid(user, income);

        income.setUser(user);

        incomeRepository.save(income);

        return income;
    }

    private void validateIfCategoryIsValid(User user, Income income) {
        if (income.getCategory().getUser().getId() != user.getId()
                || income.getCategory().getType() != CategoryTypeEnum.INCOME)
            throw new DbValueNotFoundException(getClass(), "categoryId");

    }

}
