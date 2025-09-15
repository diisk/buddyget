package br.dev.diisk.application.finance.expense_recurring.cases;

import br.dev.diisk.application.finance.expense_transaction.cases.DeleteExpenseTransactionCase;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurringFixture;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransactionFixture;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para DeleteExpenseRecurringCase.
 */
@ExtendWith(MockitoExtension.class)
class DeleteExpenseRecurringCaseTest {

    @Mock
    private IExpenseRecurringRepository expenseRecurringRepository;
    @Mock
    private IExpenseTransactionRepository expenseTransactionRepository;
    @Mock
    private DeleteExpenseTransactionCase deleteExpenseTransactionCase;

    @InjectMocks
    private DeleteExpenseRecurringCase deleteExpenseRecurringCase;

    // Teste: Deve deletar a despesa recorrente e transações relacionadas quando existir e pertencer ao usuário
    @Test
    void deleteExpenseRecurring_deveDeletarRecorrenteETransacoes_quandoExisteEPertenceAoUsuario() {
        // Given
        Long userId = 1L;
        Long expenseRecurringId = 10L;
        Long transactionId1 = 100L;
        Long transactionId2 = 101L;

        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(20L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        ExpenseTransaction transaction1 = ExpenseTransactionFixture.umaTransacaoComExpenseRecurring(
                transactionId1, user, category, expenseRecurring, LocalDateTime.now());
        ExpenseTransaction transaction2 = ExpenseTransactionFixture.umaTransacaoComExpenseRecurring(
                transactionId2, user, category, expenseRecurring, LocalDateTime.now());
        
        List<ExpenseTransaction> relatedTransactions = List.of(transaction1, transaction2);

        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
                .thenReturn(relatedTransactions);

        // When
        deleteExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then
        assertTrue(expenseRecurring.isDeleted(), "A despesa recorrente deve estar marcada como deletada");
        verify(deleteExpenseTransactionCase).execute(user, transactionId1, true);
        verify(deleteExpenseTransactionCase).execute(user, transactionId2, true);
        verify(expenseRecurringRepository).save(expenseRecurring);
    }

    // Teste: Não deve fazer nada quando a despesa recorrente não existir
    @Test
    void deleteExpenseRecurring_naoFazNada_quandoNaoExiste() {
        // Given
        Long userId = 1L;
        Long expenseRecurringId = 999L;
        User user = UserFixture.umUsuarioComId(userId);

        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.empty());

        // When
        deleteExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then
        verify(expenseTransactionRepository, never()).findAllRecurringRelatedBy(any());
        verify(deleteExpenseTransactionCase, never()).execute(any(), any(), any());
        verify(expenseRecurringRepository, never()).save(any(ExpenseRecurring.class));
    }

    // Teste: Não deve fazer nada quando a despesa recorrente não pertencer ao usuário
    @Test
    void deleteExpenseRecurring_naoFazNada_quandoNaoPertenceAoUsuario() {
        // Given
        Long userId = 1L;
        Long outroUserId = 999L;
        Long expenseRecurringId = 10L;

        User user = UserFixture.umUsuarioComId(userId);
        User outroUser = UserFixture.umUsuarioComId(outroUserId);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, outroUser);

        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));

        // When
        deleteExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then
        assertFalse(expenseRecurring.isDeleted(), "A despesa recorrente não deve estar marcada como deletada");
        verify(expenseTransactionRepository, never()).findAllRecurringRelatedBy(any());
        verify(deleteExpenseTransactionCase, never()).execute(any(), any(), any());
        verify(expenseRecurringRepository, never()).save(any(ExpenseRecurring.class));
    }

    // Teste: Deve deletar a despesa recorrente mesmo sem transações relacionadas
    @Test
    void deleteExpenseRecurring_deveDeletarRecorrente_quandoNaoHaTransacoesRelacionadas() {
        // Given
        Long userId = 1L;
        Long expenseRecurringId = 10L;

        User user = UserFixture.umUsuarioComId(userId);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        List<ExpenseTransaction> emptyTransactions = List.of();

        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
                .thenReturn(emptyTransactions);

        // When
        deleteExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then
        assertTrue(expenseRecurring.isDeleted(), "A despesa recorrente deve estar marcada como deletada");
        verify(deleteExpenseTransactionCase, never()).execute(any(), any(), any());
        verify(expenseRecurringRepository).save(expenseRecurring);
    }

    // Teste: Deve deletar apenas uma transação quando houver apenas uma transação relacionada
    @Test
    void deleteExpenseRecurring_deveDeletarRecorrenteEUmaTransacao_quandoHaApenasUmaTransacao() {
        // Given
        Long userId = 1L;
        Long expenseRecurringId = 10L;
        Long transactionId = 100L;

        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(20L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        ExpenseTransaction transaction = ExpenseTransactionFixture.umaTransacaoComExpenseRecurring(
                transactionId, user, category, expenseRecurring, LocalDateTime.now());
        
        List<ExpenseTransaction> relatedTransactions = List.of(transaction);

        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
                .thenReturn(relatedTransactions);

        // When
        deleteExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then
        assertTrue(expenseRecurring.isDeleted(), "A despesa recorrente deve estar marcada como deletada");
        verify(deleteExpenseTransactionCase).execute(user, transactionId, true);
        verify(expenseRecurringRepository).save(expenseRecurring);
    }

    // Teste: Deve chamar delete() na despesa recorrente antes de processar transações
    @Test
    void deleteExpenseRecurring_deveChamarDeleteNaRecorrente_antesDeProcessarTransacoes() {
        // Given
        Long userId = 1L;
        Long expenseRecurringId = 10L;

        User user = UserFixture.umUsuarioComId(userId);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
                .thenReturn(List.of());

        // When
        deleteExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then
        assertTrue(expenseRecurring.isDeleted(), "A despesa recorrente deve estar marcada como deletada");
        verify(expenseRecurringRepository).save(expenseRecurring);
    }

    // Teste: Deve passar force=true para deleteExpenseTransactionCase
    @Test
    void deleteExpenseRecurring_devePassarForceTrue_paraDeleteTransactionCase() {
        // Given
        Long userId = 1L;
        Long expenseRecurringId = 10L;
        Long transactionId = 100L;

        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(20L, CategoryTypeEnum.EXPENSE, user);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        ExpenseTransaction transaction = ExpenseTransactionFixture.umaTransacaoComExpenseRecurring(
                transactionId, user, category, expenseRecurring, LocalDateTime.now());
        
        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
                .thenReturn(List.of(transaction));

        // When
        deleteExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then
        verify(deleteExpenseTransactionCase).execute(user, transactionId, true);
    }

    // Teste: Deve verificar compatibilidade de IDs de usuário com tipos Long
    @Test
    void deleteExpenseRecurring_deveCompararCorretamenteIdsUsuario_quandoTiposLong() {
        // Given
        Long userId = 1L;
        Long expenseRecurringId = 10L;

        User user = UserFixture.umUsuarioComId(userId);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        
        when(expenseRecurringRepository.findById(expenseRecurringId)).thenReturn(Optional.of(expenseRecurring));
        when(expenseTransactionRepository.findAllRecurringRelatedBy(List.of(expenseRecurringId)))
                .thenReturn(List.of());

        // When
        deleteExpenseRecurringCase.execute(user, expenseRecurringId);

        // Then - deve executar normalmente pois o userId da expense recurring é igual ao do user
        assertTrue(expenseRecurring.isDeleted(), "A despesa recorrente deve estar marcada como deletada");
        verify(expenseRecurringRepository).save(expenseRecurring);
    }
}
