package br.dev.diisk.application.cases.credit_card;

import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListCreditCardsCase {

    private final ICreditCardRepository creditCardRepository;

    public List<CreditCard> execute(User user) {

        List<CreditCard> creditCards = creditCardRepository.findByUserId(user.getId());

        return creditCards;
    }

}
