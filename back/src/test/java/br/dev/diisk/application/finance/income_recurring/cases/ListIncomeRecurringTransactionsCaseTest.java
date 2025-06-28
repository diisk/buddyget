package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransactionFixture;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso ListIncomeRecurringTransactionsCase.
 * Testa todos os cenários relevantes para listagem de transações de receita recorrente.
 */
@ExtendWith(MockitoExtension.class)
class ListIncomeRecurringTransactionsCaseTest {

    @Mock
    private IIncomeTransactionRepository incomeRepository;

    @InjectMocks
    private ListIncomeRecurringTransactionsCase listIncomeRecurringTransactionsCase;

    @Test
    @DisplayName("Deve listar transações corretamente quando há transações recorrentes")
    void listIncomeRecurringTransactions_deveListarCorretamente_quandoHaTransacoesRecorrentes() {
        // Given: usuário e ID de receita recorrente válidos
        User user = UserFixture.umUsuarioComId(1L);
        Long incomeRecurringId = 10L;
        
        // Criando transações com diferentes status para testar ordenação
        IncomeTransaction transacao1 = IncomeTransactionFixture.umaIncomeTransactionComStatus(1L, "recebido");
        IncomeTransaction transacao2 = IncomeTransactionFixture.umaIncomeTransactionComStatus(2L, "pendente");
        IncomeTransaction transacao3 = IncomeTransactionFixture.umaIncomeTransactionComStatus(3L, "recebido");
        
        Set<IncomeTransaction> transacoes = Set.of(transacao1, transacao2, transacao3);
        when(incomeRepository.findByRecurring(user.getId(), incomeRecurringId)).thenReturn(transacoes);

        // When: executar o caso de uso
        List<IncomeTransaction> resultado = listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId);

        // Then: verificar se as transações foram retornadas corretamente
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(transacao1));
        assertTrue(resultado.contains(transacao2));
        assertTrue(resultado.contains(transacao3));
        
        // Verificar se o repositório foi chamado com os parâmetros corretos
        verify(incomeRepository).findByRecurring(user.getId(), incomeRecurringId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há transações recorrentes")
    void listIncomeRecurringTransactions_deveRetornarListaVazia_quandoNaoHaTransacoes() {
        // Given: usuário e ID de receita recorrente válidos mas sem transações
        User user = UserFixture.umUsuarioComId(1L);
        Long incomeRecurringId = 10L;
        
        Set<IncomeTransaction> transacoesVazias = Collections.emptySet();
        when(incomeRepository.findByRecurring(user.getId(), incomeRecurringId)).thenReturn(transacoesVazias);

        // When: executar o caso de uso
        List<IncomeTransaction> resultado = listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId);

        // Then: verificar se retorna lista vazia
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
        
        // Verificar se o repositório foi chamado com os parâmetros corretos
        verify(incomeRepository).findByRecurring(user.getId(), incomeRecurringId);
    }

    @Test
    @DisplayName("Deve ordenar transações por status corretamente - pendente primeiro")
    void listIncomeRecurringTransactions_deveOrdenarPorStatus_quandoHaTransacoesComStatusDiferentes() {
        // Given: usuário e transações com diferentes status
        User user = UserFixture.umUsuarioComId(1L);
        Long incomeRecurringId = 10L;
        
        IncomeTransaction transacaoRecebida = IncomeTransactionFixture.umaIncomeTransactionComStatus(1L, "recebido");
        IncomeTransaction transacaoPendente = IncomeTransactionFixture.umaIncomeTransactionComStatus(2L, "pendente");
        
        Set<IncomeTransaction> transacoes = Set.of(transacaoPendente, transacaoRecebida);
        when(incomeRepository.findByRecurring(user.getId(), incomeRecurringId)).thenReturn(transacoes);

        // When: executar o caso de uso
        List<IncomeTransaction> resultado = listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId);

        // Then: verificar se as transações estão ordenadas corretamente (pendente primeiro, conforme lógica de ordenação)
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Pendente", resultado.get(0).getStatus());
        assertEquals("Recebido", resultado.get(1).getStatus());
        
        // Verificar se o repositório foi chamado com os parâmetros corretos
        verify(incomeRepository).findByRecurring(user.getId(), incomeRecurringId);
    }

    @Test
    @DisplayName("Deve listar corretamente uma única transação")
    void listIncomeRecurringTransactions_deveListarCorretamente_quandoHaUmaTransacao() {
        // Given: usuário e uma única transação recorrente
        User user = UserFixture.umUsuarioComId(1L);
        Long incomeRecurringId = 10L;
        
        IncomeTransaction transacao = IncomeTransactionFixture.umaIncomeTransactionComId(1L);
        Set<IncomeTransaction> transacoes = Set.of(transacao);
        when(incomeRepository.findByRecurring(user.getId(), incomeRecurringId)).thenReturn(transacoes);

        // When: executar o caso de uso
        List<IncomeTransaction> resultado = listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId);

        // Then: verificar se a transação foi retornada corretamente
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transacao, resultado.get(0));
        assertEquals(transacao.getId(), resultado.get(0).getId());
        assertEquals(transacao.getDescription(), resultado.get(0).getDescription());
        assertEquals(transacao.getValue(), resultado.get(0).getValue());
        assertEquals(transacao.getStatus(), resultado.get(0).getStatus());
        
        // Verificar se o repositório foi chamado com os parâmetros corretos
        verify(incomeRepository).findByRecurring(user.getId(), incomeRecurringId);
    }

    @Test
    @DisplayName("Deve chamar repositório com parâmetros corretos")
    void listIncomeRecurringTransactions_deveChamarRepositorio_comParametrosCorretos() {
        // Given: usuário e ID de receita recorrente específicos
        User user = UserFixture.umUsuarioComId(5L);
        Long incomeRecurringId = 25L;
        
        Set<IncomeTransaction> transacoes = Collections.emptySet();
        when(incomeRepository.findByRecurring(user.getId(), incomeRecurringId)).thenReturn(transacoes);

        // When: executar o caso de uso
        listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId);

        // Then: verificar se o repositório foi chamado com os IDs corretos
        verify(incomeRepository, times(1)).findByRecurring(5L, 25L);
        verifyNoMoreInteractions(incomeRepository);
    }

    @Test
    @DisplayName("Deve ordenar múltiplas transações corretamente por status e data de criação")
    void listIncomeRecurringTransactions_deveOrdenarMultiplasTransacoes_quandoHaVariosStatus() {
        // Given: usuário e múltiplas transações com diferentes status
        User user = UserFixture.umUsuarioComId(1L);
        Long incomeRecurringId = 10L;
        
        IncomeTransaction transacao1Recebida = IncomeTransactionFixture.umaIncomeTransactionComStatus(1L, "recebido");
        IncomeTransaction transacao2Pendente = IncomeTransactionFixture.umaIncomeTransactionComStatus(2L, "pendente");
        IncomeTransaction transacao3Recebida = IncomeTransactionFixture.umaIncomeTransactionComStatus(3L, "recebido");
        IncomeTransaction transacao4Pendente = IncomeTransactionFixture.umaIncomeTransactionComStatus(4L, "pendente");
        
        Set<IncomeTransaction> transacoes = Set.of(transacao1Recebida, transacao2Pendente, transacao3Recebida, transacao4Pendente);
        when(incomeRepository.findByRecurring(user.getId(), incomeRecurringId)).thenReturn(transacoes);

        // When: executar o caso de uso
        List<IncomeTransaction> resultado = listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId);

        // Then: verificar se as transações estão ordenadas corretamente (pendentes primeiro, depois recebidas)
        assertNotNull(resultado);
        assertEquals(4, resultado.size());
        assertEquals("Pendente", resultado.get(0).getStatus());
        assertEquals("Pendente", resultado.get(1).getStatus());
        assertEquals("Recebido", resultado.get(2).getStatus());
        assertEquals("Recebido", resultado.get(3).getStatus());
        
        // Verificar se o repositório foi chamado com os parâmetros corretos
        verify(incomeRepository).findByRecurring(user.getId(), incomeRecurringId);
    }

    @Test
    @DisplayName("Deve listar corretamente quando todas as transações têm o mesmo status")
    void listIncomeRecurringTransactions_deveListarCorretamente_quandoTodasTransacoesMesmoStatus() {
        // Given: usuário e múltiplas transações com mesmo status
        User user = UserFixture.umUsuarioComId(1L);
        Long incomeRecurringId = 10L;
        
        IncomeTransaction transacao1 = IncomeTransactionFixture.umaIncomeTransactionComStatus(1L, "recebido");
        IncomeTransaction transacao2 = IncomeTransactionFixture.umaIncomeTransactionComStatus(2L, "recebido");
        IncomeTransaction transacao3 = IncomeTransactionFixture.umaIncomeTransactionComStatus(3L, "recebido");
        
        Set<IncomeTransaction> transacoes = Set.of(transacao1, transacao2, transacao3);
        when(incomeRepository.findByRecurring(user.getId(), incomeRecurringId)).thenReturn(transacoes);

        // When: executar o caso de uso
        List<IncomeTransaction> resultado = listIncomeRecurringTransactionsCase.execute(user, incomeRecurringId);

        // Then: verificar se todas as transações foram retornadas
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        
        // Verificar se todas têm o mesmo status
        assertTrue(resultado.stream().allMatch(t -> "Recebido".equals(t.getStatus())));
        
        // Verificar se contém todas as transações esperadas
        assertTrue(resultado.contains(transacao1));
        assertTrue(resultado.contains(transacao2));
        assertTrue(resultado.contains(transacao3));
        
        // Verificar se o repositório foi chamado com os parâmetros corretos
        verify(incomeRepository).findByRecurring(user.getId(), incomeRecurringId);
    }
}
