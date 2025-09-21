package br.dev.diisk.domain.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class Recurring extends Finance {

    @Embedded
    protected Period period;

    @Getter(value = lombok.AccessLevel.NONE)
    @Setter
    @Column(nullable = false)
    private Boolean active = true;

    public Recurring(String description, Category category, BigDecimal value, Period period,
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

    public void defineEndDate(LocalDateTime endDate) {
        if(this.period.getEndDate() != null)
            throw new BusinessException(getClass(), "Essa recorrência já possui uma data de término.");
        
        this.period = new Period(getStartDate(), endDate);
    }

    private void validatePeriod(Period period) {
        if (period == null)
            throw new NullOrEmptyException(getClass(), "period");

    }

    public Boolean isActive() {
        return active;
    }

    private void validate() {
        validatePeriod(this.period);

    }
}