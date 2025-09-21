package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.application.finance.income_recurring.dtos.EndIncomeRecurringParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransactionFixture;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EndIncomeRecurringCaseTest {

    @Mock
    private GetIncomeRecurringCase getIncomeRecurringCase;

    @Mock
    private IIncomeRecurringRepository incomeRecurringRepository;

    @Mock
    private IIncomeTransactionRepository incomeTransactionRepository;

    @InjectMocks
    private EndIncomeRecurringCase endIncomeRecurringCase;

    @Test
    void endIncomeRecurring_deveFinalizarReceita_quandoDadosValidos() {
        // Given - Prepara os dados de entrada e mocks
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.of(2024, 1, 1, 0, 0), null);
        Long incomeRecurringId = 1L;
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
                incomeRecurringId, user, category, new DayOfMonth(10), period);
        
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 0, 0);
        EndIncomeRecurringParams params = new EndIncomeRecurringParams(endDate);
        
        // Mock das transações relacionadas (sem transações pagas após a data de término)
        List<IncomeTransaction> transactions = List.of(
                IncomeTransactionFixture.umaTransacaoComPaymentDate(1L, user, category, 
                        LocalDateTime.of(2024, 5, 10, 0, 0)) // Data anterior ao término
        );

        when(getIncomeRecurringCase.execute(user, incomeRecurringId)).thenReturn(incomeRecurring);
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
                .thenReturn(transactions);
        when(incomeRecurringRepository.save(any(IncomeRecurring.class))).thenReturn(incomeRecurring);

        // When - Executa o método a ser testado
        IncomeRecurring result = endIncomeRecurringCase.execute(user, params, incomeRecurringId);

        // Then - Verifica os resultados
        assertNotNull(result);
        assertNotNull(result.getEndDate());
        assertEquals(incomeRecurring.getId(), result.getId());
        assertEquals(incomeRecurring.getDescription(), result.getDescription());
        assertEquals(incomeRecurring.getValue(), result.getValue());
        assertEquals(incomeRecurring.getCategory(), result.getCategory());
        assertEquals(incomeRecurring.getUser(), result.getUser());
        
        verify(getIncomeRecurringCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(incomeRecurringRepository).save(incomeRecurring);
    }

    @Test
    void endIncomeRecurring_deveLancarExcecao_quandoExisteTransacaoPagaAposDataTermino() {
        // Given - Prepara cenário com transação paga após a data de término
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.of(2024, 1, 1, 0, 0), null);
        Long incomeRecurringId = 1L;
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
                incomeRecurringId, user, category, new DayOfMonth(10), period);
        
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 0, 0);
        EndIncomeRecurringParams params = new EndIncomeRecurringParams(endDate);
        
        // Mock de transação paga APÓS a data de término
        List<IncomeTransaction> transactions = List.of(
                IncomeTransactionFixture.umaTransacaoComPaymentDate(1L, user, category, 
                        LocalDateTime.of(2024, 6, 20, 0, 0)) // Data posterior ao término
        );

        when(getIncomeRecurringCase.execute(user, incomeRecurringId)).thenReturn(incomeRecurring);
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
                .thenReturn(transactions);

        // When & Then - Executa e verifica a exceção
        BusinessException ex = assertThrows(BusinessException.class, () ->
                endIncomeRecurringCase.execute(user, params, incomeRecurringId)
        );
        
        assertEquals("Não é possível definir uma data de término anterior a uma despesa já paga.", ex.getMessage());
        
        verify(getIncomeRecurringCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }

    @Test
    void endIncomeRecurring_deveFinalizarReceita_quandoExisteTransacaoNaoPaga() {
        // Given - Prepara cenário com transação não paga (paymentDate null)
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.of(2024, 1, 1, 0, 0), null);
        Long incomeRecurringId = 1L;
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
                incomeRecurringId, user, category, new DayOfMonth(10), period);
        
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 0, 0);
        EndIncomeRecurringParams params = new EndIncomeRecurringParams(endDate);
        
        // Mock de transação não paga (paymentDate = null)
        List<IncomeTransaction> transactions = List.of(
                IncomeTransactionFixture.umaTransacaoComPaymentDate(1L, user, category, null)
        );

        when(getIncomeRecurringCase.execute(user, incomeRecurringId)).thenReturn(incomeRecurring);
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
                .thenReturn(transactions);
        when(incomeRecurringRepository.save(any(IncomeRecurring.class))).thenReturn(incomeRecurring);

        // When - Executa o método a ser testado
        IncomeRecurring result = endIncomeRecurringCase.execute(user, params, incomeRecurringId);

        // Then - Verifica que não houve exceção e a receita foi finalizada
        assertNotNull(result);
        assertNotNull(result.getEndDate());
        assertEquals(incomeRecurring.getId(), result.getId());
        
        verify(getIncomeRecurringCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(incomeRecurringRepository).save(incomeRecurring);
    }

    @Test
    void endIncomeRecurring_deveFinalizarReceita_quandoNaoExistemTransacoes() {
        // Given - Prepara cenário sem transações relacionadas
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.of(2024, 1, 1, 0, 0), null);
        Long incomeRecurringId = 1L;
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
                incomeRecurringId, user, category, new DayOfMonth(10), period);
        
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 0, 0);
        EndIncomeRecurringParams params = new EndIncomeRecurringParams(endDate);
        
        // Mock de lista vazia de transações
        List<IncomeTransaction> transactions = List.of();

        when(getIncomeRecurringCase.execute(user, incomeRecurringId)).thenReturn(incomeRecurring);
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
                .thenReturn(transactions);
        when(incomeRecurringRepository.save(any(IncomeRecurring.class))).thenReturn(incomeRecurring);

        // When - Executa o método a ser testado
        IncomeRecurring result = endIncomeRecurringCase.execute(user, params, incomeRecurringId);

        // Then - Verifica que a receita foi finalizada corretamente
        assertNotNull(result);
        assertNotNull(result.getEndDate());
        assertEquals(incomeRecurring.getId(), result.getId());
        assertEquals(incomeRecurring.getDescription(), result.getDescription());
        assertEquals(incomeRecurring.getValue(), result.getValue());
        assertEquals(incomeRecurring.getCategory(), result.getCategory());
        assertEquals(incomeRecurring.getUser(), result.getUser());
        
        verify(getIncomeRecurringCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(incomeRecurringRepository).save(incomeRecurring);
    }

    @Test
    void endIncomeRecurring_deveFinalizarReceita_quandoTransacaoPagaAntesDaDataTermino() {
        // Given - Prepara cenário com múltiplas transações, algumas pagas antes da data de término
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.of(2024, 1, 1, 0, 0), null);
        Long incomeRecurringId = 1L;
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
                incomeRecurringId, user, category, new DayOfMonth(10), period);
        
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 0, 0);
        EndIncomeRecurringParams params = new EndIncomeRecurringParams(endDate);
        
        // Mock com múltiplas transações válidas (todas antes ou na data de término)
        List<IncomeTransaction> transactions = List.of(
                IncomeTransactionFixture.umaTransacaoComPaymentDate(1L, user, category, 
                        LocalDateTime.of(2024, 5, 10, 0, 0)), // Antes
                IncomeTransactionFixture.umaTransacaoComPaymentDate(2L, user, category, 
                        LocalDateTime.of(2024, 6, 15, 0, 0)), // Exato na data
                IncomeTransactionFixture.umaTransacaoComPaymentDate(3L, user, category, null) // Não paga
        );

        when(getIncomeRecurringCase.execute(user, incomeRecurringId)).thenReturn(incomeRecurring);
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
                .thenReturn(transactions);
        when(incomeRecurringRepository.save(any(IncomeRecurring.class))).thenReturn(incomeRecurring);

        // When - Executa o método a ser testado
        IncomeRecurring result = endIncomeRecurringCase.execute(user, params, incomeRecurringId);

        // Then - Verifica que a receita foi finalizada sem exceções
        assertNotNull(result);
        assertNotNull(result.getEndDate());
        assertEquals(incomeRecurring.getId(), result.getId());
        assertEquals(incomeRecurring.getDescription(), result.getDescription());
        assertEquals(incomeRecurring.getValue(), result.getValue());
        
        verify(getIncomeRecurringCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(incomeRecurringRepository).save(incomeRecurring);
    }

    @Test
    void endIncomeRecurring_deveLancarExcecao_quandoRecorrenciaJaPossuiDataTermino() {
        // Given - Prepara cenário com receita recorrente que já possui data de término
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period periodWithEndDate = new Period(
                LocalDateTime.of(2024, 1, 1, 0, 0), 
                LocalDateTime.of(2024, 5, 31, 23, 59)); // Já possui data de término
        Long incomeRecurringId = 1L;
        
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(
                incomeRecurringId, user, category, new DayOfMonth(10), periodWithEndDate);
        
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 0, 0);
        EndIncomeRecurringParams params = new EndIncomeRecurringParams(endDate);
        
        // Mock de lista vazia de transações para não interferir no teste
        List<IncomeTransaction> transactions = List.of();

        when(getIncomeRecurringCase.execute(user, incomeRecurringId)).thenReturn(incomeRecurring);
        when(incomeTransactionRepository.findAllRecurringRelatedBy(List.of(incomeRecurringId)))
                .thenReturn(transactions);

        // When & Then - Executa e verifica a exceção do método defineEndDate
        BusinessException ex = assertThrows(BusinessException.class, () ->
                endIncomeRecurringCase.execute(user, params, incomeRecurringId)
        );
        
        assertEquals("Essa recorrência já possui uma data de término.", ex.getMessage());
        
        verify(getIncomeRecurringCase).execute(user, incomeRecurringId);
        verify(incomeTransactionRepository).findAllRecurringRelatedBy(List.of(incomeRecurringId));
        verify(incomeRecurringRepository, never()).save(any(IncomeRecurring.class));
    }
}
