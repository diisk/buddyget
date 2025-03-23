package br.dev.diisk.application.cases.credit_card;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCreditCardCase {

    private final ICreditCardRepository creditCardRepository;

    public CreditCard execute(Long userId, Long id) {
        CreditCard creditCard = creditCardRepository.findById(id).orElse(null);
        if(creditCard == null || creditCard.getUser().getId() != userId) 
            throw new DbValueNotFoundException(getClass(), "id");

        return creditCard;
    }

}
