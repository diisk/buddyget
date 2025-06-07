package br.dev.diisk.domain.entities.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Transaction extends GenericTransaction {

    @Column(nullable = true)
    protected LocalDateTime date;

    public Transaction(String description, Category category, BigDecimal value, LocalDateTime date, User user) {
        super(description, category, value, user);
        this.date = date;
        validate();
    }

    private void validate() {
        if (date != null && date.isAfter(LocalDateTime.now())) {
            throw new BusinessException(getClass(), "A data não pode estar no futuro",
                    Map.of("date", date.toString()));
        }
    }

}
