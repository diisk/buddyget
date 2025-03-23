package br.dev.diisk.infra.repositories.income;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;
import br.dev.diisk.infra.jpas.income.IncomeJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class IncomeRepository extends BaseRepository<IncomeJPA, Income> implements IIncomeRepository {

    public IncomeRepository(IncomeJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
