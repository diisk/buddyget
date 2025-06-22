package br.dev.diisk.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Recurring extends GenericTransaction {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "endDate", column = @Column(nullable = false))
    })
    protected DataRange period;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "recurring_day"))
    })
    protected DayOfMonth recurringDay;

    public Recurring(String description, Category category, BigDecimal value, DataRange period, User user) {
        super(description, category, value, user);
        this.period = period;
        validate();
    }

    public Integer getRecurringDayValue() {
        return recurringDay != null ? recurringDay.getValue() : null;
    }

    public LocalDateTime getStartDate() {
        return period.getStartDate();
    }

    public LocalDateTime getEndDate() {
        return period.getEndDate();
    }

    private void validate() {
        period.validateEndDate(getClass());
    }
}