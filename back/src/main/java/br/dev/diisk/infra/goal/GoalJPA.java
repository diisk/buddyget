package br.dev.diisk.infra.goal;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.goal.Goal;

public interface GoalJPA extends JpaRepository<Goal, Long> {
    // Add any custom methods if needed
}
