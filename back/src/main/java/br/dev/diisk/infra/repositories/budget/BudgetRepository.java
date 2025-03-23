package br.dev.diisk.infra.repositories.budget;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.budget.Budget;
import br.dev.diisk.domain.repositories.budget.IBudgetRepository;
import br.dev.diisk.infra.jpas.budget.BudgetJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class BudgetRepository extends BaseRepository<BudgetJPA, Budget> implements IBudgetRepository {

    public BudgetRepository(BudgetJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
