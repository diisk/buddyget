package br.dev.diisk.domain.entities.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.enums.expense.FixedExpenseTypeEnum;
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

    @Column(nullable = true)
    private Integer dueDay;

    @Column(nullable = true)
    private LocalDateTime endReference;

    @Column(nullable = true)
    private Integer totalInstallments;

    @Column(nullable = false)
    private LocalDateTime startReference;

    @Column(nullable = false)
    private FixedExpenseTypeEnum type;

    @Column(nullable = true)
    private BigDecimal totalAmount;

}
