package br.dev.diisk.domain.entities.transaction;

import java.math.BigDecimal;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.value_objects.DataRange;
import br.dev.diisk.domain.value_objects.DayOfMonth;
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

    private void validate() {
        period.validateEndDate(getClass());
    }
}