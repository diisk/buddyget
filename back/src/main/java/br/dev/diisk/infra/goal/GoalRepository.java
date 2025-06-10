package br.dev.diisk.infra.goal;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.goal.Goal;
import br.dev.diisk.domain.repositories.goal.IGoalRepository;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class GoalRepository extends BaseRepository<GoalJPA, Goal> implements IGoalRepository {

    public GoalRepository(GoalJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
