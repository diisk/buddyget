package br.dev.diisk.infra.monthly_summary;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class MonthlySummaryRepository extends BaseRepository<MonthlySummaryJPA, MonthlySummary> implements IMonthlySummaryRepository {

    public MonthlySummaryRepository(MonthlySummaryJPA jpa) {
        super(jpa);
    }

    @Override
    public Optional<MonthlySummary> findBy(Long userId, Integer month, Integer year, Long categoryId) {
        return jpa.findByUser_IdAndMonthAndYearAndCategory_IdAndDeletedFalse(userId, month, year, categoryId);
    }

}
