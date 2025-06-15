package br.dev.diisk.infra.monthly_summary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.monthly_summary.MonthlySummary;

public interface MonthlySummaryJPA extends JpaRepository<MonthlySummary, Long> {

    Optional<MonthlySummary> findByUser_IdAndMonthAndYearAndCategory_IdAndDeletedFalse(Long userId, Integer month,
            Integer year, Long categoryId);

}
