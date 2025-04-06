package br.dev.diisk.domain.entities.expense;

import java.time.LocalDateTime;

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
@Table(name = "expenses")
public class Expense extends GenericExpense {

    @Column(nullable = false)
    private LocalDateTime dueDate;
 
    @Column(nullable = false)
    private Integer installmentReference;

    @Column(nullable = true)
    private LocalDateTime paymentDate;

    @ManyToOne(optional = true)
    private FixedExpense fixedExpense;

}
