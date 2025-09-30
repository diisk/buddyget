package br.dev.diisk.application.credit_card.cases;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.credit_card.dtos.AddCreditCardParams;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.ICreditCardRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddCreditCardCase {

    private final ICreditCardRepository creditCardRepository;

    @Transactional
    public CreditCard execute(User user, AddCreditCardParams params) {
        String name = params.getName();
        DayOfMonth billDueDay = new DayOfMonth(params.getBillDueDay());
        DayOfMonth billClosingDay = new DayOfMonth(params.getBillClosingDay());
        BigDecimal cardLimit = params.getCardLimit();
        HexadecimalColor color = new HexadecimalColor(params.getColor());

        CreditCard creditCard = new CreditCard(user, name, billDueDay, billClosingDay, cardLimit, color);

        if (creditCardRepository.findBy(user.getId(), name) != null)
            throw new DatabaseValueConflictException(getClass(), "Já existe um cartão de crédito com esse nome.");

        creditCardRepository.save(creditCard);

        return creditCard;
    }
}
