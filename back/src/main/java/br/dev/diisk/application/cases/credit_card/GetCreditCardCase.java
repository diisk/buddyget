package br.dev.diisk.application.cases.credit_card;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCreditCardCase {

    private final ICreditCardRepository creditCardRepository;

    public CreditCard execute(User user, Long id) {
        CreditCard creditCard = creditCardRepository.findById(id).orElse(null);
        if(creditCard == null || creditCard.getUser().getId() != user.getId()) 
            throw new DbValueNotFoundException(getClass(), "id");

        return creditCard;
    }

}
