package br.dev.diisk.infra.jpas.goal;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.goal.Goal;

public interface GoalJPA extends JpaRepository<Goal, Long> {
    // Add any custom methods if needed
}
