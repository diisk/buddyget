package br.dev.diisk.application.cases.income;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetIncomeCase {

    private final IIncomeRepository incomeRepository;

    public Income execute(User user, Long incomeId) {
        Income income = incomeRepository.findById(incomeId).orElse(null);
        if (income == null || income.getUser().getId() != user.getId())
            throw new DbValueNotFoundException(getClass(), "id");

        return income;
    }

}
