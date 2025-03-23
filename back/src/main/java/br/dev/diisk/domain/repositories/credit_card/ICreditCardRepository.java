package br.dev.diisk.domain.repositories.credit_card;

import java.util.List;
import java.util.Optional;

import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.repositories.IBaseRepository;

public interface ICreditCardRepository extends IBaseRepository<CreditCard> {

    Optional<CreditCard> findBy(Long userId, String name);

    List<CreditCard> findByUserId(Long userId);

}
