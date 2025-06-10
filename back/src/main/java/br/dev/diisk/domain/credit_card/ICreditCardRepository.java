package br.dev.diisk.domain.credit_card;

import java.util.List;
import java.util.Optional;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface ICreditCardRepository extends IBaseRepository<CreditCard> {

    Optional<CreditCard> findBy(Long userId, String name);

    List<CreditCard> findByUserId(Long userId);

}
