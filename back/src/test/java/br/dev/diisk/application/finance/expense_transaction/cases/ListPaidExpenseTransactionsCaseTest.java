package br.dev.diisk.application.finance.expense_transaction.cases;

import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.IExpenseTransactionRepository;
import br.dev.diisk.domain.finance.expense_transaction.ListExpenseTransactionsFilter;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ListPaidExpenseTransactionsCase.
 */
@ExtendWith(MockitoExtension.class)
class ListPaidExpenseTransactionsCaseTest {

    @Mock
    private IExpenseTransactionRepository expenseRepository;

    @InjectMocks
    private ListPaidExpenseTransactionsCase listPaidExpenseTransactionsCase;

    // Teste: Deve listar transações pagas corretamente quando todas as datas são informadas
    @Test
    void listPaidExpenseTransactions_deveListarCorretamente_quandoTodasDatasInformadas() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setSearchString("teste");
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExpenseTransaction> expectedPage = new PageImpl<>(Collections.emptyList());
        
        when(expenseRepository.findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable)))
                .thenReturn(expectedPage);

        // When
        Page<ExpenseTransaction> result = listPaidExpenseTransactionsCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        verify(expenseRepository).findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable));
        
        // Verificar que as datas originais foram mantidas
        assertEquals(startDate, filter.getStartDate());
        assertEquals(endDate, filter.getEndDate());
    }

    // Teste: Deve definir endDate como agora quando não informada
    @Test
    void listPaidExpenseTransactions_deveDefinirEndDateComoAgora_quandoNaoInformada() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(startDate);
        filter.setEndDate(null);
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExpenseTransaction> expectedPage = new PageImpl<>(Collections.emptyList());
        
        when(expenseRepository.findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable)))
                .thenReturn(expectedPage);

        LocalDateTime beforeExecution = LocalDateTime.now();

        // When
        Page<ExpenseTransaction> result = listPaidExpenseTransactionsCase.execute(user, filter, pageable);

        LocalDateTime afterExecution = LocalDateTime.now();

        // Then
        assertEquals(expectedPage, result);
        assertNotNull(filter.getEndDate());
        assertTrue(filter.getEndDate().isAfter(beforeExecution.minusSeconds(1)));
        assertTrue(filter.getEndDate().isBefore(afterExecution.plusSeconds(1)));
        assertEquals(startDate, filter.getStartDate());
        verify(expenseRepository).findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable));
    }

    // Teste: Deve definir startDate como primeiro dia do mês da endDate quando não informada
    @Test
    void listPaidExpenseTransactions_deveDefinirStartDateComoPrimeiroDiaDoMes_quandoNaoInformada() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        LocalDateTime endDate = LocalDateTime.now().minusDays(15);
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(null);
        filter.setEndDate(endDate);
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExpenseTransaction> expectedPage = new PageImpl<>(Collections.emptyList());
        
        when(expenseRepository.findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable)))
                .thenReturn(expectedPage);

        // When
        Page<ExpenseTransaction> result = listPaidExpenseTransactionsCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        assertNotNull(filter.getStartDate());
        assertEquals(LocalDateTime.of(endDate.getYear(), endDate.getMonth(), 1, 0, 0), filter.getStartDate());
        assertEquals(endDate, filter.getEndDate());
        verify(expenseRepository).findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable));
    }

    // Teste: Deve definir ambas as datas quando ambas não informadas
    @Test
    void listPaidExpenseTransactions_deveDefinirAmbasAsDatas_quandoAmbasNaoInformadas() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(null);
        filter.setEndDate(null);
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExpenseTransaction> expectedPage = new PageImpl<>(Collections.emptyList());
        
        when(expenseRepository.findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable)))
                .thenReturn(expectedPage);

        LocalDateTime beforeExecution = LocalDateTime.now();

        // When
        Page<ExpenseTransaction> result = listPaidExpenseTransactionsCase.execute(user, filter, pageable);

        LocalDateTime afterExecution = LocalDateTime.now();

        // Then
        assertEquals(expectedPage, result);
        
        // Verificar endDate foi definida como agora
        assertNotNull(filter.getEndDate());
        assertTrue(filter.getEndDate().isAfter(beforeExecution.minusSeconds(1)));
        assertTrue(filter.getEndDate().isBefore(afterExecution.plusSeconds(1)));
        
        // Verificar startDate foi definida como primeiro dia do mês da endDate
        assertNotNull(filter.getStartDate());
        LocalDateTime expectedStartDate = LocalDateTime.of(filter.getEndDate().getYear(), filter.getEndDate().getMonth(), 1, 0, 0);
        assertEquals(expectedStartDate, filter.getStartDate());
        
        // Verificar que startDate é menor que agora (evita falhas de validação)
        assertTrue(filter.getStartDate().isBefore(LocalDateTime.now()));
        
        verify(expenseRepository).findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable));
    }

    // Teste: Deve lançar exceção quando startDate é maior ou igual ao momento atual
    @Test
    void listPaidExpenseTransactions_deveLancarExcecao_quandoStartDateMaiorOuIgualAgora() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(futureDate);
        filter.setEndDate(endDate);
        
        Pageable pageable = PageRequest.of(0, 10);

        // When & Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                listPaidExpenseTransactionsCase.execute(user, filter, pageable)
        );
        
        assertEquals(futureDate.toString(), ex.getDetails().get("startDate"));
        verify(expenseRepository, never()).findAllPaidBy(any(), any(), any());
    }

    // Teste: Deve funcionar corretamente quando startDate é menor que agora
    @Test
    void listPaidExpenseTransactions_deveFuncionarCorretamente_quandoStartDateMenorQueAgora() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        LocalDateTime pastStartDate = LocalDateTime.now().minusDays(7);
        LocalDateTime pastEndDate = LocalDateTime.now().minusDays(1);
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(pastStartDate);
        filter.setEndDate(pastEndDate);
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExpenseTransaction> expectedPage = new PageImpl<>(Collections.emptyList());
        
        when(expenseRepository.findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable)))
                .thenReturn(expectedPage);

        // When
        Page<ExpenseTransaction> result = listPaidExpenseTransactionsCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        assertEquals(pastStartDate, filter.getStartDate());
        assertEquals(pastEndDate, filter.getEndDate());
        verify(expenseRepository).findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable));
    }

    // Teste: Deve lançar exceção quando startDate é maior que endDate
    @Test
    void listPaidExpenseTransactions_deveLancarExcecao_quandoStartDateMaiorQueEndDate() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        LocalDateTime startDate = LocalDateTime.now().minusDays(5); // Depois
        LocalDateTime endDate = LocalDateTime.now().minusDays(10);  // Antes
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        
        Pageable pageable = PageRequest.of(0, 10);

        // When & Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                listPaidExpenseTransactionsCase.execute(user, filter, pageable)
        );
        
        assertEquals(startDate.toString(), ex.getDetails().get("startDate"));
        assertEquals(endDate.toString(), ex.getDetails().get("endDate"));
        verify(expenseRepository, never()).findAllPaidBy(any(), any(), any());
    }

    // Teste: Deve lançar exceção quando startDate é igual a endDate
    @Test
    void listPaidExpenseTransactions_deveLancarExcecao_quandoStartDateIgualEndDate() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        LocalDateTime sameDate = LocalDateTime.now().minusDays(5);
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(sameDate);
        filter.setEndDate(sameDate);
        
        Pageable pageable = PageRequest.of(0, 10);

        // When & Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                listPaidExpenseTransactionsCase.execute(user, filter, pageable)
        );
        
        assertEquals(sameDate.toString(), ex.getDetails().get("startDate"));
        assertEquals(sameDate.toString(), ex.getDetails().get("endDate"));
        verify(expenseRepository, never()).findAllPaidBy(any(), any(), any());
    }

    // Teste: Deve chamar repositório com parâmetros corretos
    @Test
    void listPaidExpenseTransactions_deveChamarRepositorioComParametrosCorretos_quandoExecucaoCorreta() {
        // Given
        User user = UserFixture.umUsuarioComId(123L);
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setSearchString("busca teste");
        
        Pageable pageable = PageRequest.of(2, 15);
        Page<ExpenseTransaction> expectedPage = new PageImpl<>(Collections.emptyList());
        
        when(expenseRepository.findAllPaidBy(eq(123L), any(ListExpenseTransactionsFilter.class), eq(pageable)))
                .thenReturn(expectedPage);

        // When
        Page<ExpenseTransaction> result = listPaidExpenseTransactionsCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        
        // Capturar e verificar o filtro passado para o repositório
        ArgumentCaptor<ListExpenseTransactionsFilter> filterCaptor = ArgumentCaptor.forClass(ListExpenseTransactionsFilter.class);
        verify(expenseRepository).findAllPaidBy(eq(123L), filterCaptor.capture(), eq(pageable));
        
        ListExpenseTransactionsFilter capturedFilter = filterCaptor.getValue();
        assertEquals(startDate, capturedFilter.getStartDate());
        assertEquals(endDate, capturedFilter.getEndDate());
        assertEquals("busca teste", capturedFilter.getSearchString());
    }

    // Teste: Deve preservar searchString do filtro original
    @Test
    void listPaidExpenseTransactions_devePreservarSearchString_quandoDefinindoDatas() {
        // Given
        User user = UserFixture.umUsuarioComId(1L);
        String searchString = "texto de busca específico";
        
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(null);
        filter.setEndDate(null);
        filter.setSearchString(searchString);
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExpenseTransaction> expectedPage = new PageImpl<>(Collections.emptyList());
        
        when(expenseRepository.findAllPaidBy(eq(user.getId()), any(ListExpenseTransactionsFilter.class), eq(pageable)))
                .thenReturn(expectedPage);

        // When
        Page<ExpenseTransaction> result = listPaidExpenseTransactionsCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        assertEquals(searchString, filter.getSearchString());
        
        ArgumentCaptor<ListExpenseTransactionsFilter> filterCaptor = ArgumentCaptor.forClass(ListExpenseTransactionsFilter.class);
        verify(expenseRepository).findAllPaidBy(eq(user.getId()), filterCaptor.capture(), eq(pageable));
        
        ListExpenseTransactionsFilter capturedFilter = filterCaptor.getValue();
        assertEquals(searchString, capturedFilter.getSearchString());
    }
}
