package br.dev.diisk.infra.monthly_summary;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.repositories.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class MonthlySummaryRepository extends BaseRepository<MonthlySummaryJPA, MonthlySummary> implements IMonthlySummaryRepository {

    public MonthlySummaryRepository(MonthlySummaryJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
