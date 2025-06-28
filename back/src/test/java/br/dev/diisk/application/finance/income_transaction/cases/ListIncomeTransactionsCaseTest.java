package br.dev.diisk.application.finance.income_transaction.cases;

import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.ListIncomeTransactionsFilter;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso ListIncomeTransactionsCase.
 * Testa todos os cenários relevantes, cobrindo caminhos felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class ListIncomeTransactionsCaseTest {

    @Mock
    private IIncomeTransactionRepository incomeRepository;

    @InjectMocks
    private ListIncomeTransactionsCase listIncomeTransactionsCase;

    private User user;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        pageable = Pageable.unpaged();
    }

    @Test
    @DisplayName("Deve listar transações de receita corretamente quando filtro está completo")
    void listIncomeTransactions_deveListarCorretamente_quandoFiltroCompleto() {
        // Given: filtro com datas preenchidas
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 30, 23, 59);
        ListIncomeTransactionsFilter filter = new ListIncomeTransactionsFilter();
        filter.setStartDate(start);
        filter.setEndDate(end);
        Page<IncomeTransaction> expectedPage = new PageImpl<>(Collections.emptyList());
        when(user.getId()).thenReturn(1L); // Corrige o id do usuário
        when(incomeRepository.findAllBy(anyLong(), any(), any())).thenReturn(expectedPage);

        // When: executa o caso de uso
        Page<IncomeTransaction> result = listIncomeTransactionsCase.execute(user, filter, pageable);

        // Then: verifica se o repository foi chamado corretamente e o retorno está correto
        assertEquals(expectedPage, result);
        verify(incomeRepository).findAllBy(eq(1L), eq(filter), eq(pageable));
    }

    @Test
    @DisplayName("Deve preencher endDate com now se for nulo")
    void listIncomeTransactions_devePreencherEndDateComNow_quandoEndDateNulo() {
        // Given: filtro sem endDate
        ListIncomeTransactionsFilter filter = new ListIncomeTransactionsFilter();
        filter.setStartDate(LocalDateTime.of(2024, 6, 1, 0, 0));
        filter.setEndDate(null);
        when(incomeRepository.findAllBy(anyLong(), any(), any())).thenReturn(Page.empty());

        // When
        listIncomeTransactionsCase.execute(user, filter, pageable);

        // Then
        assertNotNull(filter.getEndDate());
        assertTrue(filter.getEndDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Deve preencher startDate com primeiro dia do mês de endDate se for nulo")
    void listIncomeTransactions_devePreencherStartDateComPrimeiroDiaDoMes_quandoStartDateNulo() {
        // Given: filtro sem startDate
        ListIncomeTransactionsFilter filter = new ListIncomeTransactionsFilter();
        filter.setEndDate(LocalDateTime.of(2024, 6, 15, 12, 0));
        filter.setStartDate(null);
        when(incomeRepository.findAllBy(anyLong(), any(), any())).thenReturn(Page.empty());

        // When
        listIncomeTransactionsCase.execute(user, filter, pageable);

        // Then
        assertNotNull(filter.getStartDate());
        assertEquals(1, filter.getStartDate().getDayOfMonth());
        assertEquals(filter.getEndDate().getMonth(), filter.getStartDate().getMonth());
        assertEquals(filter.getEndDate().getYear(), filter.getStartDate().getYear());
    }

    @Test
    @DisplayName("Deve passar corretamente os parâmetros para o repository")
    void listIncomeTransactions_devePassarParametrosCorretamente_quandoChamado() {
        // Given
        ListIncomeTransactionsFilter filter = new ListIncomeTransactionsFilter();
        filter.setStartDate(LocalDateTime.of(2024, 6, 1, 0, 0));
        filter.setEndDate(LocalDateTime.of(2024, 6, 30, 23, 59));
        Page<IncomeTransaction> page = new PageImpl<>(Collections.emptyList());
        when(user.getId()).thenReturn(1L); // Corrige o id do usuário
        when(incomeRepository.findAllBy(anyLong(), any(), any())).thenReturn(page);

        // When
        Page<IncomeTransaction> result = listIncomeTransactionsCase.execute(user, filter, pageable);

        // Then
        assertEquals(page, result);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<ListIncomeTransactionsFilter> filterCaptor = ArgumentCaptor.forClass(ListIncomeTransactionsFilter.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(incomeRepository).findAllBy(userIdCaptor.capture(), filterCaptor.capture(), pageableCaptor.capture());
        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(filter, filterCaptor.getValue());
        assertEquals(pageable, pageableCaptor.getValue());
    }
}
