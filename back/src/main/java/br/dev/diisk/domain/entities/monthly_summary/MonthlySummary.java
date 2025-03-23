package br.dev.diisk.domain.entities.monthly_summary;

import java.math.BigDecimal;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "incomes")
public class MonthlySummary extends UserRastrableEntity {

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Long budgetLimit;

    @ManyToOne(optional = false)
    private Category category;

}
