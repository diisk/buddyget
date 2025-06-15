package br.dev.diisk.application.goal.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.goal.IGoalRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetGoalCase {

    private final IGoalRepository goalRepository;

    @Transactional
    public Goal execute(User user, Long goalId) {
        Goal goal = goalRepository.findById(goalId).orElse(null);
        if (goal == null || !goal.getUserId().equals(user.getId())) {
            throw new DatabaseValueNotFoundException(getClass(), goalId.toString());
        }
        return goal;
    }
}
