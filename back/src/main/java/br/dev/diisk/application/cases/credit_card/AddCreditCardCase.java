package br.dev.diisk.application.cases.credit_card;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.dtos.credit_card.AddCreditCardDto;
import br.dev.diisk.application.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddCreditCardCase {

    private final ModelMapper mappper;
    private final ICreditCardRepository creditCardRepository;

    
    public CreditCard execute(User user, AddCreditCardDto dto) {

        validateIfCreditCardExistsByName(user.getId(), dto.getName());

        CreditCard creditCard = mappper.map(dto, CreditCard.class);

        creditCard.setUser(user);

        creditCardRepository.save(creditCard);

        return creditCard;
    }

    private void validateIfCreditCardExistsByName(Long userId, String nomeCartao){
        if(creditCardRepository.findBy(userId, nomeCartao).isPresent())
            throw new ValueAlreadyInDatabaseException(getClass(),"name");
    }

}
