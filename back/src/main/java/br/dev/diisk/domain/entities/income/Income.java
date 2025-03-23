package br.dev.diisk.domain.entities.income;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class Income extends UserRastrableEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime receiptDate;

    @ManyToOne(optional = false)
    private Category category;

}
