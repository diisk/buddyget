package br.dev.diisk.domain.value_objects;

import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class DayOfMonth {

    @Column(name = "day_of_month", nullable = true)
    private final Integer value;

    public DayOfMonth(Integer value) {
        this.value = value;
        validate();
    }

    private void validate() {
        if (value != null && (value < 1 || value > 28)) {
            throw new BadRequestValueCustomRuntimeException(
                    getClass(), "Invalid day of month", value.toString());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        DayOfMonth that = (DayOfMonth) obj;
        return value.equals(that.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
