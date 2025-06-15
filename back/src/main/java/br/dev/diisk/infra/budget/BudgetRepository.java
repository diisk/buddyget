package br.dev.diisk.infra.budget;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.budget.Budget;
import br.dev.diisk.domain.budget.IBudgetRepository;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class BudgetRepository extends BaseRepository<BudgetJPA, Budget> implements IBudgetRepository {

    public BudgetRepository(BudgetJPA jpa) {
        super(jpa);
    }

    @Override
    public Optional<Budget> findByCategory(Long userId, Long categoryId) {
       return jpa.findByUser_IdAndCategory_Id(userId, categoryId);
    }

}
