package br.dev.diisk.domain.entities.credit_card;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
import br.dev.diisk.domain.validations.credit_card.CreditCardIfExistsNotBelongUserValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "credit_cards_bills")
public class CreditCardBill extends UserRastrableEntity {

    @ManyToOne(optional = false)
    private CreditCard creditCard;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private LocalDateTime closingDate;

    public CreditCardBill(User user, CreditCard creditCard, BigDecimal value, LocalDateTime dueDate,
            LocalDateTime closingDate) {
        super(user);
        this.creditCard = creditCard;
        this.value = value;
        this.dueDate = dueDate;
        this.closingDate = closingDate;
        validate();
    }

    private void validate() {
        List<IValidationStrategy> validations = List.of(
                new CreditCardIfExistsNotBelongUserValidation(creditCard, getUserId()));

        if (creditCard == null) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Credit card cannot be null.", null);
        }

        if (creditCard.getId() == null || creditCard.getId() <= 0) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Credit card ID is null or invalid.",
                    creditCard.getId().toString());

        }

        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Value must be greater or equal to zero.",
                    null);
        }

        if (dueDate == null) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Due date cannot be null.", null);
        }

        if (closingDate == null) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Closing date cannot be null.",
                    null);
        }

        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Due date cannot be in the past.", null);
        }

        if (closingDate.isBefore(LocalDateTime.now())) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Closing date cannot be in the past.", null);
        }

        if (closingDate.isAfter(dueDate)) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Closing date cannot be after due date.", null);
        }

        validations.forEach(validation -> validation.validate(getClass()));
    }
}