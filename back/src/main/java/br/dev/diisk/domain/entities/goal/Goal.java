package br.dev.diisk.domain.entities.goal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "goals")
public class Goal extends UserRastrableEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal targetAmount;

    @Column(nullable = false)
    private BigDecimal accumulatedAmount = BigDecimal.ZERO;

    @Column(nullable = true)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private Boolean completed = false;

    public Goal(User user, String description, BigDecimal targetAmount) {
        super(user);
        this.description = description;
        this.targetAmount = targetAmount;
        validate();
    }

    private void validate() {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }

        if (targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Target amount must be greater than zero.");
        }
    }

}
