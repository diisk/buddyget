package br.dev.diisk.domain.entities.credit_card;

import java.math.BigDecimal;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.value_objects.DayOfMonth;
import br.dev.diisk.domain.value_objects.HexadecimalColor;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "credit_cards")
public class CreditCard extends UserRastrableEntity {

    @Column(nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "bill_due_day", nullable = false))
    private DayOfMonth billDueDay;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "bill_closing_day", nullable = false))
    private DayOfMonth billClosingDay;

    @Column(nullable = false)
    private BigDecimal cardLimit;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "color", nullable = false))
    private HexadecimalColor color;


    public CreditCard(User user, String name, DayOfMonth billDueDay, DayOfMonth billClosingDay, BigDecimal cardLimit,
            HexadecimalColor color) {
        super(user);
        this.name = name;
        this.billDueDay = billDueDay;
        this.billClosingDay = billClosingDay;
        this.cardLimit = cardLimit;
        this.color = color;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Name cannot be null or empty.", name);
        }

        if (billDueDay == null) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Bill due day cannot be null.",
                    billDueDay.toString());
        }

        if (billClosingDay == null) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Bill closing day cannot be null.",
                    billClosingDay.toString());
        }

        if (cardLimit == null || cardLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Card limit must be greater than zero.",
                    cardLimit.toString());
        }

        if (color == null) {
            throw new BadRequestValueCustomRuntimeException(getClass(), "Color cannot be null.", color.toString());
        }
    }

}
