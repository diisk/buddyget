package br.dev.diisk.domain.validations.credit_card;

import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.exceptions.BusinessException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;
import java.util.Map;

public class CreditCardIfExistsNotBelongUserValidation implements IValidationStrategy {

    private final CreditCard creditCard;
    private final Long userId;

    public CreditCardIfExistsNotBelongUserValidation(CreditCard creditCard, Long userId) {
        this.creditCard = creditCard;
        this.userId = userId;
    }

    @Override
    public void validate(Class<?> classObj) {
        if (creditCard != null)
            if (creditCard.getUserId() != userId)
                throw new BusinessException(classObj, "A categoria deve pertencer ao mesmo usuário",
                        Map.of("creditCardId", creditCard.getId().toString()));
    }
}
