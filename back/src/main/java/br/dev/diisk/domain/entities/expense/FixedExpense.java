package br.dev.diisk.domain.entities.expense;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "fixed_expenses")
public class FixedExpense extends GenericExpense {

    @Column(nullable = false)
    private Integer dueDay;

    @Column(nullable = true)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private LocalDateTime startDate;

}
