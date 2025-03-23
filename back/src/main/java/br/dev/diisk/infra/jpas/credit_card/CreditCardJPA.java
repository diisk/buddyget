package br.dev.diisk.infra.jpas.credit_card;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.credit_card.CreditCard;

public interface CreditCardJPA extends JpaRepository<CreditCard, Long> {
    Optional<CreditCard> findByUser_IdAndNameAndDeletedFalse(Long userId, String name);
    List<CreditCard> findByUser_IdAndDeletedFalse(Long userId);
}
