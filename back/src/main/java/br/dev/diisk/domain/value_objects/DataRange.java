package br.dev.diisk.domain.value_objects;

import java.time.LocalDateTime;

import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class DataRange {

    @Column(name = "start_date", nullable = false)
    private final LocalDateTime startDate;
    @Column(name = "end_date", nullable = true)
    private final LocalDateTime endDate;

    public DataRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        validate();
    }

    private void validate() {
        if (startDate == null) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Start date cannot be null", null);
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "End date cannot be before start date", null);

        }
    }

    public void validateEndDate(Class<?> classObj) {
        if (endDate != null) {
            throw new BadRequestValueCustomRuntimeException(classObj, "End date cannot be null", null);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        DataRange dataRange = (DataRange) obj;
        return startDate.equals(dataRange.startDate)
                && (endDate != null ? endDate.equals(dataRange.endDate) : dataRange.endDate == null);
    }

    @Override
    public String toString() {
        return startDate.toString() + " - " + (endDate != null ? endDate.toString() : "null");
    }
}
