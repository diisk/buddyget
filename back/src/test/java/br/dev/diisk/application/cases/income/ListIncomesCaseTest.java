package br.dev.diisk.application.cases.income;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.application.UtilService;
import br.dev.diisk.application.exceptions.date.PeriodOrderException;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.incomes.ListIncomesFilter;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;

import java.util.Collections;

public class ListIncomesCaseTest {

    @Mock
    private IIncomeRepository incomeRepository;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private ListIncomesCase listIncomesCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listIncomesCase_quandoDatasValidas_DeveRetornarRendas() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        ListIncomesFilter filter = new ListIncomesFilter();
        filter.setStartReferenceDate(LocalDateTime.now().minusMonths(2));
        filter.setEndReferenceDate(LocalDateTime.now().minusMonths(1));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Income> expectedPage = new PageImpl<>(Collections.emptyList());

        when(incomeRepository.findBy(userId, filter, pageable)).thenReturn(expectedPage);

        // When
        Page<Income> result = listIncomesCase.execute(user,filter, pageable);

        // Then
        assertEquals(expectedPage, result);
    }

    @Test
    public void listIncomesCase_quandoDatasInvalidas_DeveLancarPeriodOrderException() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        ListIncomesFilter filter = new ListIncomesFilter();
        filter.setStartReferenceDate(LocalDateTime.now());
        filter.setEndReferenceDate(LocalDateTime.now().minusMonths(1));
        Pageable pageable = PageRequest.of(0, 10);

        // When / Then
        assertThrows(PeriodOrderException.class, () -> listIncomesCase.execute(user,filter, pageable));
    }

    @Test
    public void listIncomesCase_quandoOnlyPendingTrue_NaoDeveValidarDatas() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        ListIncomesFilter filter = new ListIncomesFilter();
        filter.setOnlyPending(true);
        filter.setStartReferenceDate(null);
        filter.setEndReferenceDate(null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Income> expectedPage = new PageImpl<>(Collections.emptyList());

        when(incomeRepository.findBy(userId, filter, pageable)).thenReturn(expectedPage);

        // When
        Page<Income> result = listIncomesCase.execute(user, filter, pageable);

        // Then
        assertEquals(expectedPage, result);
        assertEquals(true, filter.getOnlyPending());
        assertEquals(null, filter.getStartReferenceDate());
        assertEquals(null, filter.getEndReferenceDate());
    }
}
