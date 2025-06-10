package br.dev.diisk.infra.budget;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.budget.Budget;
import br.dev.diisk.domain.budget.IBudgetRepository;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class BudgetRepository extends BaseRepository<BudgetJPA, Budget> implements IBudgetRepository {

    public BudgetRepository(BudgetJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
