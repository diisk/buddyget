package br.dev.diisk.infra.credit_card;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.credit_card.CreditCard;

public interface CreditCardJPA extends JpaRepository<CreditCard, Long> {
    CreditCard findByUser_IdAndNameAndDeletedFalse(Long userId, String name);

    List<CreditCard> findByUser_IdAndDeletedFalse(Long userId);
}
