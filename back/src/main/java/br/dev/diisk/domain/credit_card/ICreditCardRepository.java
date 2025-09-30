package br.dev.diisk.domain.credit_card;

import java.util.List;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface ICreditCardRepository extends IBaseRepository<CreditCard> {

    CreditCard findBy(Long userId, String name);

    List<CreditCard> findByUserId(Long userId);

}
