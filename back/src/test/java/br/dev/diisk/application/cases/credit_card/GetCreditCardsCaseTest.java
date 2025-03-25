package br.dev.diisk.application.cases.credit_card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;

import java.util.Collections;
import java.util.List;

public class GetCreditCardsCaseTest {

    @Mock
    private ICreditCardRepository creditCardRepository;

    @InjectMocks
    private ListCreditCardsCase getCreditCardsCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCreditCardsCase_quandoNaoExistemCartoes_DeveRetornarListaVazia() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(creditCardRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        List<CreditCard> creditCards = getCreditCardsCase.execute(user);

        // Then
        assertEquals(0, creditCards.size());
    }

    @Test
    public void getCreditCardsCase_quandoExistemCartoes_DeveRetornarListaDeCartoes() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        CreditCard creditCard = new CreditCard();
        when(creditCardRepository.findByUserId(userId)).thenReturn(List.of(creditCard));

        // When
        List<CreditCard> creditCards = getCreditCardsCase.execute(user);

        // Then
        assertEquals(1, creditCards.size());
        assertEquals(creditCard, creditCards.get(0));
    }
}
