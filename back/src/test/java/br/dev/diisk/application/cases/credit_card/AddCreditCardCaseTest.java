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

import br.dev.diisk.application.dtos.credit_card.AddCreditCardDTO;
import br.dev.diisk.application.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.credit_card.ICreditCardRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class AddCreditCardCaseTest {

    @Mock
    private ICreditCardRepository creditCardRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private AddCreditCardCase addCreditCardCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addCreditCardCase_quandoCartaoJaExiste_DeveLancarExcecao() {
        // Given
        User user = new User();
        user.setId(1L);
        AddCreditCardDTO dto = new AddCreditCardDTO();
        dto.setName("Test Card");
        dto.setBillDueDay(15);
        dto.setBillClosingDay(10);
        dto.setCardLimit(new BigDecimal("1000.00"));
        dto.setColor("blue");
        CreditCard existingCreditCard = new CreditCard();
        existingCreditCard.setId(1L);

        when(creditCardRepository.findBy(user.getId(), dto.getName())).thenReturn(Optional.of(existingCreditCard));

        // When & Then
        assertThrows(ValueAlreadyInDatabaseException.class, () -> {
            addCreditCardCase.execute(user, dto);
        });
    }

    @Test
    public void addCreditCardCase_quandoDadosValidos_DeveAdicionarCartao() {
        // Given
        User user = new User();
        user.setId(1L);
        AddCreditCardDTO dto = new AddCreditCardDTO();
        dto.setName("Test Card");
        dto.setBillDueDay(15);
        dto.setBillClosingDay(10);
        dto.setCardLimit(new BigDecimal("1000.00"));
        dto.setColor("#FAFAFA");
        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setName(dto.getName());
        newCreditCard.setBillDueDay(dto.getBillDueDay());
        newCreditCard.setBillClosingDay(dto.getBillClosingDay());
        newCreditCard.setCardLimit(dto.getCardLimit());
        newCreditCard.setColor(dto.getColor());
        newCreditCard.setUser(user);

        when(creditCardRepository.findBy(user.getId(), dto.getName())).thenReturn(Optional.empty());
        when(mapper.map(dto, CreditCard.class)).thenReturn(newCreditCard);
        doAnswer(invocation->{
            CreditCard creditCard = invocation.getArgument(0);
            creditCard.setId(1L);
            return creditCard;
        }).when(creditCardRepository).save(newCreditCard);


        // When
        CreditCard creditCard = addCreditCardCase.execute(user, dto);

        // Then
        assertEquals(1L, creditCard.getId());
        assertEquals(dto.getName(), creditCard.getName());
        assertEquals(dto.getBillDueDay(), creditCard.getBillDueDay());
        assertEquals(dto.getBillClosingDay(), creditCard.getBillClosingDay());
        assertEquals(dto.getCardLimit(), creditCard.getCardLimit());
        assertEquals(dto.getColor(), creditCard.getColor());
        assertEquals(user, creditCard.getUser());
    }
}
