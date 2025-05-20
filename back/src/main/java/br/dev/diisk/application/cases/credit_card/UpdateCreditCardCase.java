package br.dev.diisk.application.cases.credit_card;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.dtos.credit_card.UpdateCreditCardDto;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateCreditCardCase {

    private final ICreditCardRepository creditCardRepository;
    private final ModelMapper modelMapper;
    private final GetCreditCardCase getCreditCardCase;


    public CreditCard execute(User user, Long creditCardId, UpdateCreditCardDto dto) {

        validateIfCreditCardExistsByName(user.getId(),creditCardId,dto.getName());

        CreditCard creditCard = getCreditCardCase.execute(user, creditCardId);

        modelMapper.map(dto, creditCard);

        creditCardRepository.save(creditCard);

        return creditCard;
    }

    private void validateIfCreditCardExistsByName(Long userId, Long creditCardId, String nomeCartao){
        CreditCard creditCard = creditCardRepository.findBy(userId, nomeCartao).orElse(null);
        if(creditCard!=null && creditCard.getId()!=creditCardId)
            throw new ValueAlreadyInDatabaseException(getClass(),"name");
    }

}
