package br.dev.diisk.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class Transaction extends GenericTransaction {

    @Column(nullable = true)
    protected LocalDateTime date;

    public Transaction(String description, Category category, BigDecimal value, LocalDateTime date, User user) {
        super(description, category, value, user);
        this.date = date;
        validate();
    }

    public void update(String description, BigDecimal value, LocalDateTime date) {
        super.update(description, value);
        validateDate(date);
        this.date = date;
    }

    private void validate() {
        validateDate(this.date);
    }

    private void validateDate(LocalDateTime date) {
        if (date != null && date.isAfter(LocalDateTime.now())) {
            throw new BusinessException(getClass(), "A data não pode estar no futuro",
                    Map.of("date", date.toString()));
        }
    }

}
