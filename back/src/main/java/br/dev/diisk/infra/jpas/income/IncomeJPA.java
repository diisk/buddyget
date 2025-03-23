package br.dev.diisk.infra.jpas.income;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.income.Income;

public interface IncomeJPA extends JpaRepository<Income, Long> {
    // Add any custom methods if needed
}
