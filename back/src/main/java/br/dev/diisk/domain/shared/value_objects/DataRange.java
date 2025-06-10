package br.dev.diisk.domain.shared.value_objects;

import java.time.LocalDateTime;
import java.util.Map;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class DataRange {

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;

    public DataRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        validate();
    }

    private void validate() {
        if (startDate == null) {
            throw new NullOrEmptyException(getClass(), "startDate");
        }

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BusinessException(getClass(), "A data final não pode ser anterior à data inicial.", Map
                    .of("startDate", startDate.toLocalDate().toString(), "endDate", endDate.toLocalDate().toString()));

        }
    }

    public void validateEndDate(Class<?> classObj) {
        if (endDate != null) {
            throw new NullOrEmptyException(getClass(), "endDate");
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
