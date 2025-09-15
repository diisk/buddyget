package br.dev.diisk.application.finance.expense_recurring.cases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.application.finance.expense_recurring.dtos.EndExpenseRecurringParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurringFixture;
import br.dev.diisk.domain.finance.expense_recurring.IExpenseRecurringRepository;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransactionFixture;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

@ExtendWith(MockitoExtension.class)
class EndExpenseRecurringCaseTest {

    @Mock
    private GetExpenseRecurringCase getExpenseRecurringCase;

    @Mock
    private IExpenseRecurringRepository expenseRecurringRepository;

    @Mock
    private IExpenseTransactionRepository expenseTransactionRepository;

    @InjectMocks
    private EndExpenseRecurringCase endExpenseRecurringCase;

    @Test
    void endExpenseRecurring_deveDefinirDataFimERetornarExpenseRecurringSalva_quandoExpenseRecurringValidaEExiste() {
        // Given - Prepara user, expense recurring sem data fim, params e mock dos métodos
        User user = UserFixture.umUsuarioComId(1L);
        Long expenseRecurringId = 100L;
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComIdSemDataFim(expenseRecurringId, user);
        EndExpenseRecurringParams params = new EndExpenseRecurringParams(endDate);
        
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(expenseTransactionRepository.findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId)))
                .thenReturn(Collections.emptyList());
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenReturn(expenseRecurring);

        LocalDateTime dataAntes = LocalDateTime.now();

        // When - Executa o método de encerrar expense recurring
        ExpenseRecurring resultado = endExpenseRecurringCase.execute(user, params, expenseRecurringId);

        LocalDateTime dataDepois = LocalDateTime.now();

        // Then - Verifica se a data fim foi definida, se o save foi chamado e se retornou o objeto correto
        assertNotNull(resultado);
        assertEquals(expenseRecurring.getId(), resultado.getId());
        assertNotNull(resultado.getEndDate());
        
        // Verifica se a data fim está entre antes e depois da execução
        LocalDateTime dataFim = resultado.getEndDate();
        assert dataFim.isAfter(dataAntes.minusSeconds(1)) && dataFim.isBefore(dataDepois.plusSeconds(1));
        
        verify(getExpenseRecurringCase, times(1)).execute(user, expenseRecurringId);
        verify(expenseTransactionRepository, times(1)).findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId));
        verify(expenseRecurringRepository, times(1)).save(expenseRecurring);
    }

    @Test
    void endExpenseRecurring_deveLancarDatabaseValueNotFoundException_quandoExpenseRecurringNaoExiste() {
        // Given - Prepara user, id inexistente, params e mock que lança exceção
        User user = UserFixture.umUsuarioComId(1L);
        Long expenseRecurringIdInexistente = 999L;
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        EndExpenseRecurringParams params = new EndExpenseRecurringParams(endDate);
        
        when(getExpenseRecurringCase.execute(user, expenseRecurringIdInexistente))
                .thenThrow(new DatabaseValueNotFoundException(GetExpenseRecurringCase.class, expenseRecurringIdInexistente.toString()));

        // When & Then - Executa o método e verifica se lança a exceção esperada com detalhes corretos
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                endExpenseRecurringCase.execute(user, params, expenseRecurringIdInexistente)
        );
        
        assertEquals(expenseRecurringIdInexistente.toString(), ex.getDetails().get("valor"));
        
        verify(getExpenseRecurringCase, times(1)).execute(user, expenseRecurringIdInexistente);
        verify(expenseTransactionRepository, times(0)).findAllRecurringRelatedBy(any());
        verify(expenseRecurringRepository, times(0)).save(any(ExpenseRecurring.class));
    }

    @Test
    void endExpenseRecurring_deveLancarDatabaseValueNotFoundException_quandoExpenseRecurringNaoPertenceAoUsuario() {
        // Given - Prepara user, outro user, params e expense recurring que não pertence ao primeiro user
        User user = UserFixture.umUsuarioComId(1L);
        Long expenseRecurringId = 100L;
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        EndExpenseRecurringParams params = new EndExpenseRecurringParams(endDate);
        
        when(getExpenseRecurringCase.execute(user, expenseRecurringId))
                .thenThrow(new DatabaseValueNotFoundException(GetExpenseRecurringCase.class, expenseRecurringId.toString()));

        // When & Then - Executa o método e verifica se lança a exceção esperada com detalhes corretos
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                endExpenseRecurringCase.execute(user, params, expenseRecurringId)
        );
        
        assertEquals(expenseRecurringId.toString(), ex.getDetails().get("valor"));
        
        verify(getExpenseRecurringCase, times(1)).execute(user, expenseRecurringId);
        verify(expenseTransactionRepository, times(0)).findAllRecurringRelatedBy(any());
        verify(expenseRecurringRepository, times(0)).save(any(ExpenseRecurring.class));
    }

    @Test
    void endExpenseRecurring_deveLancarBusinessException_quandoExpenseRecurringJaPossuiDataFim() {
        // Given - Prepara user, expense recurring que já possui data fim, params e mock dos métodos
        User user = UserFixture.umUsuarioComId(1L);
        Long expenseRecurringId = 100L;
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComId(expenseRecurringId, user);
        EndExpenseRecurringParams params = new EndExpenseRecurringParams(endDate);
        
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(expenseTransactionRepository.findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId)))
                .thenReturn(Collections.emptyList());

        // When & Then - Executa o método e verifica se lança BusinessException quando tenta definir data fim novamente
        BusinessException ex = assertThrows(BusinessException.class, () ->
                endExpenseRecurringCase.execute(user, params, expenseRecurringId)
        );
        
        // A exceção é lançada pela entidade na regra de negócio do defineEndDate
        assertNotNull(ex);
        
        verify(getExpenseRecurringCase, times(1)).execute(user, expenseRecurringId);
        verify(expenseTransactionRepository, times(1)).findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId));
        verify(expenseRecurringRepository, times(0)).save(any(ExpenseRecurring.class));
    }

    @Test
    void endExpenseRecurring_deveLancarBusinessException_quandoExisteTransacaoPagaPosteriorADataFim() {
        // Given - Prepara user, expense recurring válida, params com data anterior a transação paga
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        Long expenseRecurringId = 100L;
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 0, 0);
        LocalDateTime paymentDatePosterior = LocalDateTime.of(2024, 6, 20, 0, 0);
        
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComIdSemDataFim(expenseRecurringId, user);
        ExpenseTransaction transacaoPaga = ExpenseTransactionFixture.umaTransacaoComPaymentDate(1L, user, category, paymentDatePosterior);
        EndExpenseRecurringParams params = new EndExpenseRecurringParams(endDate);
        
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(expenseTransactionRepository.findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId)))
                .thenReturn(Arrays.asList(transacaoPaga));

        // When & Then - Executa o método e verifica se lança BusinessException
        BusinessException ex = assertThrows(BusinessException.class, () ->
                endExpenseRecurringCase.execute(user, params, expenseRecurringId)
        );
        
        // Verifica que a mensagem de exceção está correta
        assertEquals("Não é possível definir uma data de término anterior a uma despesa já paga.", ex.getMessage());
        
        verify(getExpenseRecurringCase, times(1)).execute(user, expenseRecurringId);
        verify(expenseTransactionRepository, times(1)).findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId));
        verify(expenseRecurringRepository, times(0)).save(any(ExpenseRecurring.class));
    }

    @Test
    void endExpenseRecurring_devePermitirEncerramento_quandoNaoExistemTransacoesPagasPosteriores() {
        // Given - Prepara user, expense recurring válida, params com data posterior às transações pagas
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        Long expenseRecurringId = 100L;
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 25, 0, 0);
        LocalDateTime paymentDateAnterior = LocalDateTime.of(2024, 6, 15, 0, 0);
        
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComIdSemDataFim(expenseRecurringId, user);
        ExpenseTransaction transacaoPaga = ExpenseTransactionFixture.umaTransacaoComPaymentDate(1L, user, category, paymentDateAnterior);
        EndExpenseRecurringParams params = new EndExpenseRecurringParams(endDate);
        
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(expenseTransactionRepository.findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId)))
                .thenReturn(Arrays.asList(transacaoPaga));
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenReturn(expenseRecurring);

        LocalDateTime dataAntes = LocalDateTime.now();

        // When - Executa o método de encerrar expense recurring
        ExpenseRecurring resultado = endExpenseRecurringCase.execute(user, params, expenseRecurringId);

        LocalDateTime dataDepois = LocalDateTime.now();

        // Then - Verifica se a data fim foi definida e o save foi chamado
        assertNotNull(resultado);
        assertEquals(expenseRecurring.getId(), resultado.getId());
        assertNotNull(resultado.getEndDate());
        
        // Verifica se a data fim está entre antes e depois da execução
        LocalDateTime dataFim = resultado.getEndDate();
        assert dataFim.isAfter(dataAntes.minusSeconds(1)) && dataFim.isBefore(dataDepois.plusSeconds(1));
        
        verify(getExpenseRecurringCase, times(1)).execute(user, expenseRecurringId);
        verify(expenseTransactionRepository, times(1)).findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId));
        verify(expenseRecurringRepository, times(1)).save(expenseRecurring);
    }

    @Test
    void endExpenseRecurring_devePermitirEncerramento_quandoExistemTransacoesPendentesSemPaymentDate() {
        // Given - Prepara user, expense recurring válida, params e transações pendentes sem paymentDate
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        Long expenseRecurringId = 100L;
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 25, 0, 0);
        
        ExpenseRecurring expenseRecurring = ExpenseRecurringFixture.umaExpenseRecurringComIdSemDataFim(expenseRecurringId, user);
        ExpenseTransaction transacaoPendente = ExpenseTransactionFixture.umaTransacaoComId(1L, user, category); // sem paymentDate
        EndExpenseRecurringParams params = new EndExpenseRecurringParams(endDate);
        
        when(getExpenseRecurringCase.execute(user, expenseRecurringId)).thenReturn(expenseRecurring);
        when(expenseTransactionRepository.findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId)))
                .thenReturn(Arrays.asList(transacaoPendente));
        when(expenseRecurringRepository.save(any(ExpenseRecurring.class))).thenReturn(expenseRecurring);

        LocalDateTime dataAntes = LocalDateTime.now();

        // When - Executa o método de encerrar expense recurring
        ExpenseRecurring resultado = endExpenseRecurringCase.execute(user, params, expenseRecurringId);

        LocalDateTime dataDepois = LocalDateTime.now();

        // Then - Verifica se a data fim foi definida e o save foi chamado (transações pendentes não impedem encerramento)
        assertNotNull(resultado);
        assertEquals(expenseRecurring.getId(), resultado.getId());
        assertNotNull(resultado.getEndDate());
        
        // Verifica se a data fim está entre antes e depois da execução
        LocalDateTime dataFim = resultado.getEndDate();
        assert dataFim.isAfter(dataAntes.minusSeconds(1)) && dataFim.isBefore(dataDepois.plusSeconds(1));
        
        verify(getExpenseRecurringCase, times(1)).execute(user, expenseRecurringId);
        verify(expenseTransactionRepository, times(1)).findAllRecurringRelatedBy(Arrays.asList(expenseRecurringId));
        verify(expenseRecurringRepository, times(1)).save(expenseRecurring);
    }
}
