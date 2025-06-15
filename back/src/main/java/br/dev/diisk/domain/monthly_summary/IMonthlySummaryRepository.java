package br.dev.diisk.domain.monthly_summary;

import java.util.Optional;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IMonthlySummaryRepository extends IBaseRepository<MonthlySummary> {
    
    Optional<MonthlySummary> findBy(Long userId, Integer month, Integer year, Long categoryId);

}
