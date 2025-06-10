package br.dev.diisk.domain.credit_card;

import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.interfaces.IValidationStrategy;

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
