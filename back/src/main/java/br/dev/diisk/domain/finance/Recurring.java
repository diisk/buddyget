package br.dev.diisk.domain.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class Recurring extends Finance {

    @Embedded
    protected DataRange period;

    public Recurring(String description, Category category, BigDecimal value, DataRange period,
            User user) {
        super(description, category, value, user);
        this.period = period;
        validate();
    }

    void update(String description) {
        super.update(description, null);
    }

    public LocalDateTime getStartDate() {
        return period.getStartDate();
    }

    public LocalDateTime getEndDate() {
        return period.getEndDate();
    }

    private void validatePeriod(DataRange period) {
        if (period == null)
            throw new NullOrEmptyException(getClass(), "period");

    }

    private void validate() {
        validatePeriod(this.period);

    }
}