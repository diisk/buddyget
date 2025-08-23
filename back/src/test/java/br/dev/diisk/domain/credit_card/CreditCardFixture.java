package br.dev.diisk.domain.credit_card;

import java.math.BigDecimal;

import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;
import br.dev.diisk.domain.user.User;

public class CreditCardFixture {
    public static CreditCard umCartaoComId(Long id, User user) {
        CreditCard creditCard = new CreditCard(
                user,
                "Cartão Teste",
                new DayOfMonth(10),
                new DayOfMonth(5),
                new BigDecimal("1000.00"),
                new HexadecimalColor("#FFFFFF"));
        creditCard.setId(id);
        return creditCard;
    }

    public static CreditCard umCartaoComIdEBillDueDay(Long id, User user, DayOfMonth billDueDay) {
        CreditCard creditCard = new CreditCard(
                user,
                "Cartão Teste",
                billDueDay,
                new DayOfMonth(5),
                new BigDecimal("1000.00"),
                new HexadecimalColor("#FFFFFF"));
        creditCard.setId(id);
        return creditCard;
    }
}
