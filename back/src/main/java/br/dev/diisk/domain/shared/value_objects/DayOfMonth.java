package br.dev.diisk.domain.shared.value_objects;

import java.util.Map;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class DayOfMonth {

    @Column(name = "day_of_month", nullable = true)
    private Integer value;

    public DayOfMonth(Integer value) {
        this.value = value;
        validate();
    }

    private void validate() {
        Integer minValue = 1;
        Integer maxValue = 28;
        if (value != null && (value < minValue || value > maxValue)) {
            throw new BusinessException(
                    getClass(), "O dia do mês deve estar entre " + minValue + " e " + maxValue + ".",
                    Map.of("valor", value.toString()));
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
