package br.dev.diisk.application.credit_card.cases;

import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.ICreditCardRepository;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListCreditCardsCase {

    private final ICreditCardRepository creditCardRepository;

    @Transactional
    public List<CreditCard> execute(User user) {
        return creditCardRepository.findByUserId(user.getId());
    }
}
