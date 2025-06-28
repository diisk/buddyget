package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransactionFixture;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para DeleteIncomeRecurringCase.
 */
@ExtendWith(MockitoExtension.class)
class DeleteIncomeRecurringCaseTest {

    @Mock
    private IIncomeRecurringRepository incomeRecurringRepository;

    @Mock
    private IIncomeTransactionRepository incomeTransactionRepository;

    @Mock
    private ListIncomeRecurringTransactionsCase listIncomeRecurringTransactionsCase;

    @InjectMocks
    private DeleteIncomeRecurringCase deleteIncomeRecurringCase;

    @Test
    @DisplayName("Deve deletar receita recorrente e suas transações com sucesso quando dados válidos")
    void deleteIncomeRecurring_deveDeletarComSucesso_quandoDadosValidos() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 10L;
        Long incomeRecurringId = 1L;
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        DataRange period = new DataRange(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(incomeRecurringId, user,
                category, recurringDay, period);

        IncomeTransaction transaction1 = IncomeTransactionFixture.umaIncomeTransactionComId(1L);
        IncomeTransaction transaction2 = IncomeTransactionFixture.umaIncomeTransactionComId(2L);
        List<IncomeTransaction> transactions = Arrays.asList(transaction1, transaction2);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));
        when(listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId)).thenReturn(transactions);

        // When - Executa o método a ser testado
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then - Verifica os resultados usando assertions
        // Comentário: Verifica se a receita recorrente foi marcada como deletada
        assertTrue(incomeRecurring.isDeleted());

        // Comentário: Verifica se todas as transações foram marcadas como deletadas
        assertTrue(transaction1.isDeleted());
        assertTrue(transaction2.isDeleted());

        // Comentário: Verifica se os métodos dos repositórios foram chamados corretamente
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(listIncomeRecurringTransactionsCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).save(transactions);
        verify(incomeRecurringRepository).save(incomeRecurring);
    }

    @Test
    @DisplayName("Deve deletar receita recorrente sem transações quando não há transações associadas")
    void deleteIncomeRecurring_deveDeletarSemTransacoes_quandoNaoHaTransacoesAssociadas() {
        // Given - Prepara o cenário onde não há transações associadas
        Long userId = 10L;
        Long incomeRecurringId = 1L;
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        DataRange period = new DataRange(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(incomeRecurringId, user,
                category, recurringDay, period);

        List<IncomeTransaction> emptyTransactions = Arrays.asList();

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));
        when(listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId)).thenReturn(emptyTransactions);

        // When - Executa o método a ser testado
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then - Verifica os resultados usando assertions
        // Comentário: Verifica se a receita recorrente foi marcada como deletada
        assertTrue(incomeRecurring.isDeleted());

        // Comentário: Verifica se os métodos foram chamados corretamente
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(listIncomeRecurringTransactionsCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).save(emptyTransactions);
        verify(incomeRecurringRepository).save(incomeRecurring);
    }

    @Test
    @DisplayName("Deve retornar sem fazer nada quando receita recorrente não for encontrada")
    void deleteIncomeRecurring_deveRetornarSemFazerNada_quandoReceitaRecorrenteNaoEncontrada() {
        // Given - Prepara o cenário onde a receita recorrente não existe
        Long userId = 10L;
        Long incomeRecurringId = 999L;
        User user = UserFixture.umUsuarioComId(userId);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.empty());

        // When - Executa o método a ser testado
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then - Verifica os resultados usando assertions
        // Comentário: Verifica se apenas o método de busca foi chamado
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verifyNoInteractions(listIncomeRecurringTransactionsCase);
        verifyNoInteractions(incomeTransactionRepository);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    @Test
    @DisplayName("Deve retornar sem fazer nada quando receita recorrente não pertence ao usuário")
    void deleteIncomeRecurring_deveRetornarSemFazerNada_quandoReceitaRecorrenteNaoPertenceAoUsuario() {
        // Given - Prepara o cenário onde a receita recorrente pertence a outro usuário
        Long userId = 10L;
        Long outroUserId = 99L;
        Long incomeRecurringId = 1L;
        User user = UserFixture.umUsuarioComId(userId);
        User outroUser = UserFixture.umUsuarioComId(outroUserId);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, outroUser);
        DataRange period = new DataRange(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(incomeRecurringId, outroUser,
                category, recurringDay, period);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));

        // When - Executa o método a ser testado
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then - Verifica os resultados usando assertions
        // Comentário: Verifica se apenas o método de busca foi chamado quando usuário não tem acesso
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verifyNoInteractions(listIncomeRecurringTransactionsCase);
        verifyNoInteractions(incomeTransactionRepository);
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    @Test
    @DisplayName("Deve deletar uma única transação quando receita recorrente tem apenas uma transação")
    void deleteIncomeRecurring_deveDeletarUmaTransacao_quandoReceitaTemApenasumaTransacao() {
        // Given - Prepara o cenário com apenas uma transação
        Long userId = 10L;
        Long incomeRecurringId = 1L;
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        DataRange period = new DataRange(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(incomeRecurringId, user,
                category, recurringDay, period);

        IncomeTransaction singleTransaction = IncomeTransactionFixture.umaIncomeTransactionComId(1L);
        List<IncomeTransaction> transactions = Arrays.asList(singleTransaction);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));
        when(listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId)).thenReturn(transactions);

        // When - Executa o método a ser testado
        deleteIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then - Verifica os resultados usando assertions
        // Comentário: Verifica se a receita recorrente foi marcada como deletada
        assertTrue(incomeRecurring.isDeleted());

        // Comentário: Verifica se a única transação foi marcada como deletada
        assertTrue(singleTransaction.isDeleted());

        // Comentário: Verifica se os métodos dos repositórios foram chamados corretamente
        verify(incomeRecurringRepository).findById(incomeRecurringId);
        verify(listIncomeRecurringTransactionsCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).save(transactions);
        verify(incomeRecurringRepository).save(incomeRecurring);
    }
}
