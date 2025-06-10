package br.dev.diisk.infra.monthly_summary;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.monthly_summary.MonthlySummary;

public interface MonthlySummaryJPA extends JpaRepository<MonthlySummary, Long> {
    // Add any custom methods if needed
}
