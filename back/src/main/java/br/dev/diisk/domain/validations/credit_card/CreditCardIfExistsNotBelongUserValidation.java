package br.dev.diisk.domain.validations.credit_card;

import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.exceptions.BadRequestValueCustomRuntimeException;
import br.dev.diisk.domain.interfaces.IValidationStrategy;

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
                throw new BadRequestValueCustomRuntimeException(classObj, "Category must belong to the same user",
                        null);

    }

}
