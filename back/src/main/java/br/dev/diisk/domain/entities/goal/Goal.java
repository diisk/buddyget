package br.dev.diisk.domain.entities.goal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.entities.UserRastrableEntity;
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
@Table(name = "goals")
public class Goal extends UserRastrableEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal targetAmount;

    @Column(nullable = false)
    private BigDecimal accumulatedAmount;

    @Column(nullable = true)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private Boolean canceled;

}
