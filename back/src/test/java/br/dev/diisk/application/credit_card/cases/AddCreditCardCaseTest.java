package br.dev.diisk.application.credit_card.cases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.application.credit_card.dtos.AddCreditCardParams;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.credit_card.CreditCardFixture;
import br.dev.diisk.domain.credit_card.ICreditCardRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

@ExtendWith(MockitoExtension.class)
class AddCreditCardCaseTest {

    @Mock
    private ICreditCardRepository creditCardRepository;

    @InjectMocks
    private AddCreditCardCase addCreditCardCase;

    @Test
    void addCreditCard_deveCriarCartaoCorretamente_quandoDadosValidos() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "Cartão Nubank",
            10,
            5,
            new BigDecimal("2000.00"),
            "#8A05BE"
        );
        
        when(creditCardRepository.findBy(user.getId(), params.getName())).thenReturn(null);
        
        // When
        CreditCard result = addCreditCardCase.execute(user, params);
        
        // Then
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals("Cartão Nubank", result.getName());
        assertEquals(10, result.getBillDueDay().getValue());
        assertEquals(5, result.getBillClosingDay().getValue());
        assertEquals(new BigDecimal("2000.00"), result.getCardLimit());
        assertEquals("#8A05BE", result.getColor().getValue());
        
        verify(creditCardRepository).findBy(user.getId(), params.getName());
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    void addCreditCard_deveLancarExcecao_quandoNomeJaExiste() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "Cartão Existente",
            10,
            5,
            new BigDecimal("1500.00"),
            "#FF0000"
        );
        
        CreditCard cartaoExistente = CreditCardFixture.umCartaoComId(2L, user);
        when(creditCardRepository.findBy(user.getId(), params.getName())).thenReturn(cartaoExistente);
        
        // When & Then
        DatabaseValueConflictException ex = assertThrows(DatabaseValueConflictException.class, () ->
            addCreditCardCase.execute(user, params)
        );
        
        assertEquals("Já existe um cartão de crédito com esse nome.", ex.getDetails().get("valor"));
        
        verify(creditCardRepository).findBy(user.getId(), params.getName());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    void addCreditCard_deveCriarCartaoCorretamente_quandoValoresLimite() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "Cartão Limite",
            28, // Valor máximo para dia do mês
            1,  // Valor mínimo para dia do mês
            new BigDecimal("0.01"), // Valor mínimo positivo
            "#000000"
        );
        
        when(creditCardRepository.findBy(user.getId(), params.getName())).thenReturn(null);
        
        // When
        CreditCard result = addCreditCardCase.execute(user, params);
        
        // Then
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals("Cartão Limite", result.getName());
        assertEquals(28, result.getBillDueDay().getValue());
        assertEquals(1, result.getBillClosingDay().getValue());
        assertEquals(new BigDecimal("0.01"), result.getCardLimit());
        assertEquals("#000000", result.getColor().getValue());
        
        verify(creditCardRepository).findBy(user.getId(), params.getName());
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    void addCreditCard_deveCriarCartaoCorretamente_quandoLimiteAlto() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "Cartão Premium",
            15,
            10,
            new BigDecimal("100000.00"), // Limite alto
            "#FFD700"
        );
        
        when(creditCardRepository.findBy(user.getId(), params.getName())).thenReturn(null);
        
        // When
        CreditCard result = addCreditCardCase.execute(user, params);
        
        // Then
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals("Cartão Premium", result.getName());
        assertEquals(15, result.getBillDueDay().getValue());
        assertEquals(10, result.getBillClosingDay().getValue());
        assertEquals(new BigDecimal("100000.00"), result.getCardLimit());
        assertEquals("#FFD700", result.getColor().getValue());
        
        verify(creditCardRepository).findBy(user.getId(), params.getName());
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    void addCreditCard_deveCriarCartaoCorretamente_quandoNomeComEspacos() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "   Cartão com Espaços   ",
            20,
            15,
            new BigDecimal("3000.00"),
            "#00FF00"
        );
        
        when(creditCardRepository.findBy(user.getId(), params.getName())).thenReturn(null);
        
        // When
        CreditCard result = addCreditCardCase.execute(user, params);
        
        // Then
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals("   Cartão com Espaços   ", result.getName());
        assertEquals(20, result.getBillDueDay().getValue());
        assertEquals(15, result.getBillClosingDay().getValue());
        assertEquals(new BigDecimal("3000.00"), result.getCardLimit());
        verify(creditCardRepository).findBy(user.getId(), params.getName());
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    void addCreditCard_deveLancarExcecao_quandoDiaVencimentoInvalido() {
        // Given - Testando dia do mês inválido (maior que 28)
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "Cartão Inválido",
            29, // Dia inválido
            5,
            new BigDecimal("1000.00"),
            "#FF0000"
        );
        
        // When & Then
        assertThrows(Exception.class, () ->
            addCreditCardCase.execute(user, params)
        );
        
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    void addCreditCard_deveLancarExcecao_quandoDiaFechamentoInvalido() {
        // Given - Testando dia do mês inválido (menor que 1)
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "Cartão Inválido",
            10,
            0, // Dia inválido
            new BigDecimal("1000.00"),
            "#FF0000"
        );
        
        // When & Then
        assertThrows(Exception.class, () ->
            addCreditCardCase.execute(user, params)
        );
        
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    void addCreditCard_deveLancarExcecao_quandoCorInvalida() {
        // Given - Testando cor hexadecimal inválida
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "Cartão Cor Inválida",
            10,
            5,
            new BigDecimal("1000.00"),
            "cor_invalida" // Cor inválida
        );
        
        // When & Then
        assertThrows(Exception.class, () ->
            addCreditCardCase.execute(user, params)
        );
        
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    void addCreditCard_deveCriarCartaoCorretamente_quandoCorHexadecimalCurta() {
        // Given - Testando cor hexadecimal no formato curto (#FFF)
        User user = UserFixture.umUsuarioComId(1L);
        AddCreditCardParams params = new AddCreditCardParams(
            "Cartão Cor Curta",
            10,
            5,
            new BigDecimal("1000.00"),
            "#FFF"
        );
        
        when(creditCardRepository.findBy(user.getId(), params.getName())).thenReturn(null);
        
        // When
        CreditCard result = addCreditCardCase.execute(user, params);
        
        // Then
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals("Cartão Cor Curta", result.getName());
        assertEquals(10, result.getBillDueDay().getValue());
        assertEquals(5, result.getBillClosingDay().getValue());
        assertEquals(new BigDecimal("1000.00"), result.getCardLimit());
        assertEquals("#FFF", result.getColor().getValue());
        
        verify(creditCardRepository).findBy(user.getId(), params.getName());
        verify(creditCardRepository).save(any(CreditCard.class));
    }
}
