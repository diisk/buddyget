package br.dev.diisk.application.cases.credit_card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;

import java.util.Optional;

public class GetCreditCardCaseTest {

    @Mock
    private ICreditCardRepository creditCardRepository;

    @InjectMocks
    private GetCreditCardCase getCreditCardCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCreditCardCase_quandoCartaoNaoExiste_DeveLancarExcecao() {
        // Given
        Long userId = 1L;
        Long cardId = 1L;
        when(creditCardRepository.findById(cardId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DbValueNotFoundException.class, () -> {
            getCreditCardCase.execute(userId, cardId);
        });
    }

    @Test
    public void getCreditCardCase_quandoCartaoNaoPertenceAoUsuario_DeveLancarExcecao() {
        // Given
        Long userId = 1L;
        Long cardId = 1L;
        User anotherUser = new User();
        anotherUser.setId(2L);
        CreditCard creditCard = new CreditCard();
        creditCard.setId(cardId);
        creditCard.setUser(anotherUser);

        when(creditCardRepository.findById(cardId)).thenReturn(Optional.of(creditCard));

        // When & Then
        assertThrows(DbValueNotFoundException.class, () -> {
            getCreditCardCase.execute(userId, cardId);
        });
    }

    @Test
    public void getCreditCardCase_quandoCartaoExiste_DeveRetornarCartao() {
        // Given
        Long userId = 1L;
        Long cardId = 1L;
        User user = new User();
        user.setId(userId);
        CreditCard creditCard = new CreditCard();
        creditCard.setId(cardId);
        creditCard.setUser(user);

        when(creditCardRepository.findById(cardId)).thenReturn(Optional.of(creditCard));

        // When
        CreditCard result = getCreditCardCase.execute(userId, cardId);

        // Then
        assertEquals(cardId, result.getId());
        assertEquals(user, result.getUser());
    }
}
