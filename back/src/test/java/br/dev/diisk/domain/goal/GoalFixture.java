package br.dev.diisk.domain.goal;

import java.math.BigDecimal;

import br.dev.diisk.domain.user.User;

public class GoalFixture {
    public static Goal umGoalComId(Long id, User user) {
        Goal goal = new Goal(user, "Meta Teste", new BigDecimal("1000.00"));
        goal.setId(id);
        return goal;
    }
}
