package br.dev.diisk.domain.credit_card;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import br.dev.diisk.domain.shared.entities.UserRastrableEntity;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;
import br.dev.diisk.domain.user.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
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

    public String getColorString() {
        return color != null ? color.getValue() : null;
    }

    public LocalDateTime getBillDueDate(LocalDateTime referenceDate) {
        if (billDueDay == null)
            return null;
        if (referenceDate == null)
            referenceDate = LocalDateTime.now();

        LocalDateTime billDueDate = referenceDate.withDayOfMonth(billDueDay.getValue());

        return billDueDate;
    }

    public LocalDateTime getBillClosingDate(LocalDateTime referenceDate) {
        if (billClosingDay == null)
            return null;
        if (referenceDate == null)
            referenceDate = LocalDateTime.now();

        LocalDateTime billClosingDate = referenceDate.withDayOfMonth(billClosingDay.getValue());

        return billClosingDate;
    }

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
        if (name == null || name.isBlank())
            throw new NullOrEmptyException(getClass(), "name");

        if (billDueDay == null)
            throw new NullOrEmptyException(getClass(), "billDueDay");

        if (billClosingDay == null)
            throw new NullOrEmptyException(getClass(), "billClosingDay");

        if (cardLimit == null || cardLimit.compareTo(BigDecimal.ZERO) <= 0)
            throw new BusinessException(getClass(), "O limite do cartão de crédito deve ser maior que zero.",
                    Map.of("cardLimit", cardLimit != null ? cardLimit.toString() : "null"));

        if (color == null)
            throw new NullOrEmptyException(getClass(), "color");

        if (billClosingDay.getValue() == billDueDay.getValue()) {
            throw new BusinessException(getClass(),
                    "O dia de fechamento da fatura não pode ser igual ao dia de vencimento.",
                    Map.of("billClosingDay", billClosingDay.getValue().toString(),
                            "billDueDay", billDueDay.getValue().toString()));

        }

    }

}
