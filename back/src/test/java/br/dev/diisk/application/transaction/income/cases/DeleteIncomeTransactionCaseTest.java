package br.dev.diisk.application.transaction.income.cases;

import br.dev.diisk.application.monthly_summary.cases.RemoveMonthlySummaryValueCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.transaction.income.IIncomeTransactionRepository;
import br.dev.diisk.domain.transaction.income.entities.IncomeTransaction;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para DeleteIncomeTransactionCase.
 */
@ExtendWith(MockitoExtension.class)
class DeleteIncomeTransactionCaseTest {

    @Mock
    private IIncomeTransactionRepository incomeRepository;
    @Mock
    private RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;
    @Mock
    private IncomeTransaction incomeTransaction;
    @Mock
    private Category category;
    @InjectMocks
    private DeleteIncomeTransactionCase deleteIncomeTransactionCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
    }

    /**
     * Deve deletar a transação de receita e atualizar o resumo mensal quando a transação existir e pertencer ao usuário.
     */
    @Test
    void deleteIncomeTransaction_deveDeletarEAtualizarResumo_quandoTransacaoExisteEPertenceAoUsuario() {
        Long transactionId = 10L;
        LocalDateTime date = LocalDateTime.of(2024, 6, 1, 12, 0);
        BigDecimal value = BigDecimal.valueOf(100);
        when(incomeRepository.findById(transactionId)).thenReturn(Optional.of(incomeTransaction));
        when(incomeTransaction.getUserId()).thenReturn(1L);
        when(incomeTransaction.getDate()).thenReturn(date);
        when(incomeTransaction.getCategory()).thenReturn(category);
        when(incomeTransaction.getValue()).thenReturn(value);

        deleteIncomeTransactionCase.execute(user, transactionId);

        // Verifica se o método delete foi chamado
        verify(incomeTransaction).delete();
        // Verifica se a transação foi salva
        verify(incomeRepository).save(incomeTransaction);
        // Verifica se o resumo mensal foi atualizado corretamente
        ArgumentCaptor<AddMonthlySummaryValueParams> captor = ArgumentCaptor.forClass(AddMonthlySummaryValueParams.class);
        verify(removeMonthlySummaryValueCase).execute(eq(user), captor.capture());
        AddMonthlySummaryValueParams params = captor.getValue();
        assertEquals(date.getMonthValue(), params.getMonth());
        assertEquals(date.getYear(), params.getYear());
        assertEquals(value, params.getValue());
        assertEquals(category, params.getCategory());
    }

    /**
     * Não deve fazer nada se a transação não existir.
     */
    @Test
    void deleteIncomeTransaction_naoFazNada_quandoTransacaoNaoExiste() {
        Long transactionId = 20L;
        when(incomeRepository.findById(transactionId)).thenReturn(Optional.empty());

        deleteIncomeTransactionCase.execute(user, transactionId);

        // Nenhuma ação deve ser realizada
        verify(incomeTransaction, never()).delete();
        verify(incomeRepository, never()).save((IncomeTransaction) any());
        verify(removeMonthlySummaryValueCase, never()).execute(any(), any());
    }

    /**
     * Não deve fazer nada se a transação não pertencer ao usuário.
     */
    @Test
    void deleteIncomeTransaction_naoFazNada_quandoTransacaoNaoPertenceAoUsuario() {
        Long transactionId = 30L;
        when(incomeRepository.findById(transactionId)).thenReturn(Optional.of(incomeTransaction));
        when(incomeTransaction.getUserId()).thenReturn(2L); // usuário diferente

        deleteIncomeTransactionCase.execute(user, transactionId);

        verify(incomeTransaction, never()).delete();
        verify(incomeRepository, never()).save((IncomeTransaction) any());
        verify(removeMonthlySummaryValueCase, never()).execute(any(), any());
    }

    /**
     * Deve deletar a transação mas não atualizar o resumo mensal se a data for nula.
     */
    @Test
    void deleteIncomeTransaction_deveDeletarSemAtualizarResumo_quandoDataForNula() {
        Long transactionId = 40L;
        when(incomeRepository.findById(transactionId)).thenReturn(Optional.of(incomeTransaction));
        when(incomeTransaction.getUserId()).thenReturn(1L);
        when(incomeTransaction.getDate()).thenReturn(null);

        deleteIncomeTransactionCase.execute(user, transactionId);

        verify(incomeTransaction).delete();
        verify(incomeRepository).save(incomeTransaction);
        verify(removeMonthlySummaryValueCase, never()).execute(any(), any());
    }
}
