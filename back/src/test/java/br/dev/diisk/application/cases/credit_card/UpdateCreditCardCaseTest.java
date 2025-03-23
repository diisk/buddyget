package br.dev.diisk.application.cases.credit_card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.dev.diisk.application.dtos.credit_card.UpdateCreditCardDTO;
import br.dev.diisk.application.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;

import java.util.Optional;

public class UpdateCreditCardCaseTest {

    @Mock
    private ICreditCardRepository creditCardRepository;

    @Mock
    private GetCreditCardCase getCreditCardCase;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UpdateCreditCardCase updateCreditCardCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateCreditCardCase_quandoCartaoJaExiste_DeveLancarExcecao() {
        // Given
        Long userId = 1L;
        Long creditCardId = 1L;
        UpdateCreditCardDTO dto = new UpdateCreditCardDTO();
        dto.setName("Existing Card");

        CreditCard existingCreditCard = new CreditCard();
        existingCreditCard.setId(2L);

        CreditCard creditCard = new CreditCard();
        creditCard.setId(creditCardId);

        when(getCreditCardCase.execute(userId, creditCardId)).thenReturn(creditCard);
        when(creditCardRepository.findBy(userId, dto.getName())).thenReturn(Optional.of(existingCreditCard));

        // When & Then
        assertThrows(ValueAlreadyInDatabaseException.class, () -> {
            updateCreditCardCase.execute(userId, creditCardId, dto);
        });
    }

    @Test
    public void updateCreditCardCase_quandoDadosValidos_DeveAtualizarCartao() {
        // Given
        Long userId = 1L;
        Long creditCardId = 1L;
        UpdateCreditCardDTO dto = new UpdateCreditCardDTO();
        dto.setName("Updated Card");

        User user = new User();
        user.setId(userId);
        CreditCard creditCard = new CreditCard();
        creditCard.setId(creditCardId);
        creditCard.setUser(user);

        when(getCreditCardCase.execute(userId, creditCardId)).thenReturn(creditCard);
        when(creditCardRepository.findBy(userId, dto.getName())).thenReturn(Optional.empty());
        doAnswer(invocation -> {
            CreditCard c = invocation.getArgument(1);
            c.setName(dto.getName());
            return null;
        }).when(modelMapper).map(dto, creditCard);

        // When
        CreditCard updatedCreditCard = updateCreditCardCase.execute(userId, creditCardId, dto);

        // Then
        assertEquals(dto.getName(), updatedCreditCard.getName());
    }

    @Test
    public void updateCreditCardCase_quandoCartaoNaoEncontrado_DeveLancarExcecao() {
        // Given
        Long userId = 1L;
        Long creditCardId = 1L;
        UpdateCreditCardDTO dto = new UpdateCreditCardDTO();
        dto.setName("Non-existent Card");

        when(getCreditCardCase.execute(userId, creditCardId)).thenThrow(new DbValueNotFoundException(GetCreditCardCase.class,"id"));

        // When & Then
        assertThrows(DbValueNotFoundException.class, () -> {
            updateCreditCardCase.execute(userId, creditCardId, dto);
        });
    }
}
