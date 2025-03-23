package br.dev.diisk.application.cases.credit_card;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteCreditCardCase {

    private final ICreditCardRepository creditCardRepository;

    public void execute(Long userId, Long creditCardId) {
        CreditCard creditCard = creditCardRepository.findById(creditCardId).orElse(null);
        if (creditCard != null && creditCard.getUser().getId() == userId)
            creditCardRepository.delete(creditCard);

    }

}
