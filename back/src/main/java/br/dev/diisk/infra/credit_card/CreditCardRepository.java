package br.dev.diisk.infra.credit_card;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.ICreditCardRepository;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class CreditCardRepository extends BaseRepository<CreditCardJPA, CreditCard> implements ICreditCardRepository {

    public CreditCardRepository(CreditCardJPA jpa) {
        super(jpa);
    }

    @Override
    public Optional<CreditCard> findBy(Long userId, String name) {
        return jpa.findByUser_IdAndNameAndDeletedFalse(userId, name);
    }

    @Override
    public List<CreditCard> findByUserId(Long userId) {
        return jpa.findByUser_IdAndDeletedFalse(userId);
    }

}
