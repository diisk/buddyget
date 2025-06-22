package br.dev.diisk.application.credit_card.cases;

import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.ICreditCardRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso GetCreditCardCase.
 */
@ExtendWith(MockitoExtension.class)
class GetCreditCardCaseTest {

    @Mock
    private ICreditCardRepository creditCardRepository;

    @InjectMocks
    private GetCreditCardCase getCreditCardCase;

    private User user;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        creditCard = mock(CreditCard.class);
        lenient().when(user.getId()).thenReturn(1L);
        lenient().when(creditCard.getUserId()).thenReturn(1L);
    }

    /**
     * Testa o caminho feliz: deve retornar o cartão de crédito quando encontrado e
     * pertence ao usuário.
     */
    @Test
    void getCreditCard_deveRetornarCartao_quandoEncontradoEUsuarioCorreto() {
        // Given
        when(creditCardRepository.findById(10L)).thenReturn(Optional.of(creditCard));
        // When
        CreditCard result = getCreditCardCase.execute(user, 10L);
        // Then
        assertNotNull(result);
        assertEquals(creditCard, result);
        verify(creditCardRepository).findById(10L);
        verify(creditCard).getUserId();
    }

    /**
     * Testa o caso em que o cartão não é encontrado: deve lançar
     * DatabaseValueNotFoundException.
     */
    @Test
    void getCreditCard_deveLancarExcecao_quandoCartaoNaoEncontrado() {
        // Given
        when(creditCardRepository.findById(20L)).thenReturn(Optional.empty());
        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class,
                () -> getCreditCardCase.execute(user, 20L));
        assertTrue(ex.getDetails().get("valor").equals("20"));
    }

    /**
     * Testa o caso em que o cartão existe mas pertence a outro usuário: deve lançar
     * DatabaseValueNotFoundException.
     */
    @Test
    void getCreditCard_deveLancarExcecao_quandoCartaoNaoPertenceAoUsuario() {
        // Given
        when(creditCardRepository.findById(30L)).thenReturn(Optional.of(creditCard));
        when(creditCard.getUserId()).thenReturn(2L); // usuário diferente
        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class,
                () -> getCreditCardCase.execute(user, 30L));
        assertTrue(ex.getDetails().get("valor").equals("30"));
    }
}
