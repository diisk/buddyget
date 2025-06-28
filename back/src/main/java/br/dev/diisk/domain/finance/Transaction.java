package br.dev.diisk.domain.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class Transaction extends Finance {

    @Column(nullable = true)
    @Getter(value = AccessLevel.NONE)
    protected LocalDateTime date;

    @Column(nullable = true)
    protected LocalDateTime recurringReferenceDate;

    @ManyToOne(optional = true)
    private Goal goal;

    public Transaction(String description, Category category, BigDecimal value, LocalDateTime date, User user) {
        super(description, category, value, user);
        this.date = date;
        validate();
    }

    protected void update(String description, BigDecimal value, LocalDateTime date) {
        super.update(description, value);
        validateDate(date);
        this.date = date;
    }

    public void addRecurringDate(LocalDateTime recurringReferenceDate) {
        if (this.recurringReferenceDate != null)
            throw new BusinessException(getClass(), "A data de referência da recorrencia já foi definida.");

        if (recurringReferenceDate == null)
            throw new NullOrEmptyException(getClass(), "recurringReferenceDate");

        this.recurringReferenceDate = recurringReferenceDate;
    }

    public void addGoal(Goal goal) {
        if (this.goal != null)
            throw new BusinessException(getClass(), "A meta já foi definida");

        if (goal == null)
            throw new NullOrEmptyException(getClass(), "goal");

        validateGoal(goal);

        this.goal = goal;
    }

    private void validate() {
        validateDate(this.date);
        validateGoal(this.goal);
    }

    private void validateGoal(Goal goal) {
        if (goal != null && goal.getUserId() != getUserId()) {
            throw new BusinessException(getClass(),
                    "A meta não pertence ao usuário",
                    Map.of("goalId", goal.getId().toString()));
        }
    }

    private void validateDate(LocalDateTime date) {
        if (date != null && date.isAfter(LocalDateTime.now())) {
            throw new BusinessException(getClass(), "A data não pode estar no futuro",
                    Map.of("date", date.toString()));
        }
    }

}
