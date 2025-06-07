package br.dev.diisk.domain.entities.credit_card;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.BusinessException;
import br.dev.diisk.domain.exceptions.NullOrEmptyException;
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

        if (creditCard == null)
            throw new NullOrEmptyException(getClass(), "creditCard");

        if (creditCard.getId() == null || creditCard.getId() <= 0)
            throw new BusinessException(getClass(), "ID do cartão de crédito inválido.",
                    Map.of("creditCardId", creditCard.getId() != null ? creditCard.getId().toString() : "null"));

        if (value == null || value.compareTo(BigDecimal.ZERO) < 0)
            throw new BusinessException(getClass(), "O valor precisa ser maior ou igual a zero.",
                    Map.of("value", value != null ? value.toString() : "null"));

        if (dueDate == null)
            throw new NullOrEmptyException(getClass(), "dueDate");

        if (closingDate == null)
            throw new NullOrEmptyException(getClass(), "closingDate");

        if (dueDate.isBefore(LocalDateTime.now()))
            throw new BusinessException(getClass(), "A data de vencimento não pode estar no passado.",
                    Map.of("dueDate", dueDate.toString()));

        if (closingDate.isBefore(LocalDateTime.now()))
            throw new BusinessException(getClass(), "A data de fechamento não pode estar no passado.",
                    Map.of("closingDate", closingDate.toString()));

        if (closingDate.isAfter(dueDate))
            throw new BusinessException(getClass(), "A data de fechamento não pode ser posterior à data de vencimento.", Map.of(
                    "dueDate", dueDate.toString(),
                    "closingDate", closingDate.toString()));

        validations.forEach(validation -> validation.validate(getClass()));
    }
}