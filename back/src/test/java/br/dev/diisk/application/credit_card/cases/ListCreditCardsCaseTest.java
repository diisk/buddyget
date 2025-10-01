package br.dev.diisk.application.credit_card.cases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.CreditCardFixture;
import br.dev.diisk.domain.credit_card.ICreditCardRepository;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

/**
 * Testes unitários para o caso de uso ListCreditCardsCase.
 */
@ExtendWith(MockitoExtension.class)
class ListCreditCardsCaseTest {

    @Mock
    private ICreditCardRepository creditCardRepository;

    @InjectMocks
    private ListCreditCardsCase listCreditCardsCase;

    /**
     * Deve retornar lista de cartões de crédito quando usuário possui cartões.
     */
    @Test
    void listCreditCards_deveRetornarListaCartoes_quandoUsuarioPossuiCartoes() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        CreditCard cartao1 = CreditCardFixture.umCartaoComId(10L, user);
        CreditCard cartao2 = CreditCardFixture.umCartaoComId(20L, user);
        List<CreditCard> cartoesEsperados = Arrays.asList(cartao1, cartao2);
        
        when(creditCardRepository.findByUserId(user.getId())).thenReturn(cartoesEsperados);

        // When
        List<CreditCard> result = listCreditCardsCase.execute(user);

        // Then
        assertNotNull(result, "A lista de cartões não deve ser nula");
        assertEquals(2, result.size(), "Deve retornar exatamente 2 cartões");
        assertEquals(cartoesEsperados, result, "A lista deve conter os cartões esperados");
        assertTrue(result.contains(cartao1), "A lista deve conter o primeiro cartão");
        assertTrue(result.contains(cartao2), "A lista deve conter o segundo cartão");
        verify(creditCardRepository).findByUserId(user.getId());
    }

    /**
     * Deve retornar lista vazia quando usuário não possui cartões de crédito.
     */
    @Test
    void listCreditCards_deveRetornarListaVazia_quandoUsuarioNaoPossuiCartoes() {
        // Given
        User user = UserFixture.umUsuarioComId(2L);
        List<CreditCard> listaVazia = Collections.emptyList();
        
        when(creditCardRepository.findByUserId(user.getId())).thenReturn(listaVazia);

        // When
        List<CreditCard> result = listCreditCardsCase.execute(user);

        // Then
        assertNotNull(result, "A lista não deve ser nula");
        assertTrue(result.isEmpty(), "A lista deve estar vazia");
        assertEquals(0, result.size(), "O tamanho da lista deve ser 0");
        verify(creditCardRepository).findByUserId(user.getId());
    }

    /**
     * Deve retornar lista com um único cartão quando usuário possui apenas um cartão.
     */
    @Test
    void listCreditCards_deveRetornarListaComUmCartao_quandoUsuarioPossuiApenasUmCartao() {
        // Given
        User user = UserFixture.umUsuarioComId(3L);
        CreditCard cartaoUnico = CreditCardFixture.umCartaoComId(30L, user);
        List<CreditCard> listaComUmCartao = Arrays.asList(cartaoUnico);
        
        when(creditCardRepository.findByUserId(user.getId())).thenReturn(listaComUmCartao);

        // When
        List<CreditCard> result = listCreditCardsCase.execute(user);

        // Then
        assertNotNull(result, "A lista não deve ser nula");
        assertEquals(1, result.size(), "Deve retornar exatamente 1 cartão");
        assertEquals(cartaoUnico, result.get(0), "O cartão retornado deve ser o mesmo cartão único");
        assertTrue(result.contains(cartaoUnico), "A lista deve conter o cartão único");
        verify(creditCardRepository).findByUserId(user.getId());
    }

    /**
     * Deve chamar o repositório com o ID correto do usuário.
     */
    @Test
    void listCreditCards_deveChamarRepositorioComIdCorreto_quandoExecutado() {
        // Given
        Long userId = 999L;
        User user = UserFixture.umUsuarioComId(userId);
        
        when(creditCardRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        listCreditCardsCase.execute(user);

        // Then
        verify(creditCardRepository).findByUserId(userId);
    }
}
