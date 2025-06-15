package br.dev.diisk.application.credit_card.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.ICreditCardRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCreditCardCase {

    private final ICreditCardRepository creditCardRepository;

    @Transactional
    public CreditCard execute(User user, Long creditCardId) {
        CreditCard creditCard = creditCardRepository.findById(creditCardId).orElse(null);
        if (creditCard == null || !creditCard.getUserId().equals(user.getId())) {
            throw new DatabaseValueNotFoundException(getClass(), creditCardId.toString());
        }
        return creditCard;
    }
}
