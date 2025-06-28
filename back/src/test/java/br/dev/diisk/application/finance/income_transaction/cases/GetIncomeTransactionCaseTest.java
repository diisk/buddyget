package br.dev.diisk.application.finance.income_transaction.cases;

import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para GetIncomeTransactionCase.
 */
@ExtendWith(MockitoExtension.class)
class GetIncomeTransactionCaseTest {

    @Mock
    private IIncomeTransactionRepository incomeRepository;

    @InjectMocks
    private GetIncomeTransactionCase getIncomeTransactionCase;

    private User user;
    private final Long transactionId = 1L;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        // Removido o stub global de user.getId() para evitar UnnecessaryStubbingException
    }

    @Test
    @DisplayName("Deve retornar a transação de renda corretamente quando encontrada e pertence ao usuário")
    void getIncomeTransaction_deveRetornarTransacao_quandoEncontradaEPertenceAoUsuario() {
        // Given
        when(user.getId()).thenReturn(10L);
        IncomeTransaction transaction = mock(IncomeTransaction.class);
        when(transaction.getUserId()).thenReturn(10L);
        when(incomeRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // When
        IncomeTransaction result = getIncomeTransactionCase.execute(user, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(transaction, result);
        verify(incomeRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Deve lançar DatabaseValueNotFoundException quando a transação não for encontrada")
    void getIncomeTransaction_deveLancarExcecao_quandoTransacaoNaoEncontrada() {
        // Given
        when(incomeRepository.findById(transactionId)).thenReturn(Optional.empty());

        // When / Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getIncomeTransactionCase.execute(user, transactionId)
        );
        // Comentário: Verifica se a exceção é lançada quando a transação não existe
        assertTrue(ex.getDetails().get("valor").equals(transactionId.toString()));
    }

    @Test
    @DisplayName("Deve lançar DatabaseValueNotFoundException quando a transação não pertence ao usuário")
    void getIncomeTransaction_deveLancarExcecao_quandoTransacaoNaoPertenceAoUsuario() {
        // Given
        when(user.getId()).thenReturn(10L);
        IncomeTransaction transaction = mock(IncomeTransaction.class);
        when(transaction.getUserId()).thenReturn(99L); // ID diferente do usuário
        when(incomeRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // When / Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getIncomeTransactionCase.execute(user, transactionId)
        );
        // Comentário: Verifica se a exceção é lançada quando a transação não pertence ao usuário
        assertTrue(ex.getDetails().get("valor").equals(transactionId.toString()));
    }
}
