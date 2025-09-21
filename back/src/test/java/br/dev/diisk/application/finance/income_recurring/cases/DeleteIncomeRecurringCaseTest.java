package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.application.finance.income_transaction.cases.DeleteIncomeTransactionCase;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransactionFixture;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para DeleteIncomeRecurringCase.
 * Segue o padrão Given-When-Then e cobre todos os cenários possíveis.
 */
@ExtendWith(MockitoExtension.class)
class DeleteIncomeRecurringCaseTest {

    @Mock
    private IIncomeRecurringRepository incomeRecurringRepository;

    @Mock
    private IIncomeTransactionRepository incomeTransactionRepository;

    @Mock
    private DeleteIncomeTransactionCase deleteIncomeTransactionCase;

    @InjectMocks
    private DeleteIncomeRecurringCase deleteIncomeRecurringCase;

    /**
     * Deve deletar a receita recorrente e suas transações relacionadas quando todos os dados forem válidos.
     */
    @Test
    void deleteIncomeRecurring_deveDeletarRecorrenteETransacoesRelacionadas_quandoTodosOsDadosForemValidos() {
        // Given: usuário e receita recorrente válidos com transações relacionadas
        Long userId = 1L;
        Long incomeRecurringId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        
        IncomeRecurring incomeRecurring = mock(IncomeRecurring.class);
        when(incomeRecurring.getUserId()).thenReturn(userId);

        IncomeTransaction transaction1 = IncomeTransactionFixture.umaIncomeTransactionComId(20L);
        IncomeTransaction transaction2 = IncomeTransactionFixture.umaIncomeTransactionComId(21L);
        List<IncomeTransaction> relatedTransactions = Arrays.asList(transaction1, transaction2);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));
        when(incomeTransactionRepository.findAllRecurringRelatedBy(Arrays.asList(incomeRecurringId)))
                .thenReturn(relatedTransactions);

        // When: executa o caso de uso
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then: verifica que a receita recorrente foi marcada como deletada e salva
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(incomeRecurring).delete();
        verify(incomeRecurringRepository).save(incomeRecurring);
        
        // Verifica que as transações relacionadas foram deletadas
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(Arrays.asList(incomeRecurringId));
        verify(deleteIncomeTransactionCase).execute(user, 20L, true);
        verify(deleteIncomeTransactionCase).execute(user, 21L, true);
    }

    /**
     * Deve deletar a receita recorrente sem transações relacionadas quando não existirem transações.
     */
    @Test
    void deleteIncomeRecurring_deveDeletarApenasRecorrente_quandoNaoExistiremTransacoesRelacionadas() {
        // Given: usuário e receita recorrente válidos sem transações relacionadas
        Long userId = 1L;
        Long incomeRecurringId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        
        IncomeRecurring incomeRecurring = mock(IncomeRecurring.class);
        when(incomeRecurring.getUserId()).thenReturn(userId);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));
        when(incomeTransactionRepository.findAllRecurringRelatedBy(Arrays.asList(incomeRecurringId)))
                .thenReturn(Collections.emptyList());

        // When: executa o caso de uso
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then: verifica que apenas a receita recorrente foi marcada como deletada e salva
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(incomeRecurring).delete();
        verify(incomeRecurringRepository).save(incomeRecurring);
        
        // Verifica que nenhuma transação foi deletada
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(Arrays.asList(incomeRecurringId));
        verify(deleteIncomeTransactionCase, never()).execute(any(User.class), any(Long.class), any(Boolean.class));
    }

    /**
     * Não deve fazer nada quando a receita recorrente não for encontrada.
     */
    @Test
    void deleteIncomeRecurring_naoDeveFazerNada_quandoRecorrenteNaoForEncontrada() {
        // Given: ID de receita recorrente que não existe
        Long userId = 1L;
        Long incomeRecurringId = 999L;
        User user = UserFixture.umUsuarioComId(userId);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.empty());

        // When: executa o caso de uso
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then: verifica que nenhuma ação foi executada
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
        verify(incomeTransactionRepository, never()).findAllRecurringRelatedBy(any());
        verify(deleteIncomeTransactionCase, never()).execute(any(User.class), any(Long.class), any(Boolean.class));
    }

    /**
     * Não deve fazer nada quando a receita recorrente não pertencer ao usuário.
     */
    @Test
    void deleteIncomeRecurring_naoDeveFazerNada_quandoRecorrenteNaoPertenceAoUsuario() {
        // Given: usuário e receita recorrente de outro usuário
        Long userId = 1L;
        Long outroUserId = 2L;
        Long incomeRecurringId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        
        IncomeRecurring incomeRecurring = mock(IncomeRecurring.class);
        when(incomeRecurring.getUserId()).thenReturn(outroUserId);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));

        // When: executa o caso de uso
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then: verifica que nenhuma ação foi executada
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
        verify(incomeTransactionRepository, never()).findAllRecurringRelatedBy(any());
        verify(deleteIncomeTransactionCase, never()).execute(any(User.class), any(Long.class), any(Boolean.class));
    }

    /**
     * Deve tratar corretamente quando o parâmetro user for null.
     */
    @Test
    void deleteIncomeRecurring_naoDeveFazerNada_quandoUserForNull() {
        // Given: user null e ID válido
        Long incomeRecurringId = 10L;
        User user = null;

        // When: executa o caso de uso
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then: verifica que nenhuma ação foi executada para evitar NullPointerException
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
        verify(incomeTransactionRepository, never()).findAllRecurringRelatedBy(any());
        verify(deleteIncomeTransactionCase, never()).execute(any(User.class), any(Long.class), any(Boolean.class));
    }

    /**
     * Deve tratar corretamente quando o parâmetro incomeRecurringId for null.
     */
    @Test
    void deleteIncomeRecurring_naoDeveFazerNada_quandoIncomeRecurringIdForNull() {
        // Given: user válido e ID null
        Long userId = 1L;
        Long incomeRecurringId = null;
        User user = UserFixture.umUsuarioComId(userId);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.empty());

        // When: executa o caso de uso
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then: verifica que nenhuma ação foi executada
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
        verify(incomeTransactionRepository, never()).findAllRecurringRelatedBy(any());
        verify(deleteIncomeTransactionCase, never()).execute(any(User.class), any(Long.class), any(Boolean.class));
    }
}
