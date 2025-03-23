package br.dev.diisk.infra.repositories.monthly_summary;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.repositories.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.infra.jpas.monthly_summary.MonthlySummaryJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class MonthlySummaryRepository extends BaseRepository<MonthlySummaryJPA, MonthlySummary> implements IMonthlySummaryRepository {

    public MonthlySummaryRepository(MonthlySummaryJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
