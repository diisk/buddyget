package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
import br.dev.diisk.domain.finance.income_recurring.ListIncomeRecurringsFilter;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso ListIncomeRecurringsCase.
 * Testa todos os cenários relevantes, cobrindo caminhos felizes e casos
 * limites.
 */
@ExtendWith(MockitoExtension.class)
class ListIncomeRecurringsCaseTest {

    @Mock
    private IIncomeRecurringRepository incomeRecurringRepository;

    @InjectMocks
    private ListIncomeRecurringsCase listIncomeRecurringsCase;

    @Test
    @DisplayName("Deve listar receitas recorrentes corretamente quando há resultados")
    void listIncomeRecurrings_deveListarCorretamente_quandoHaResultados() {
        // Given - Preparar os dados de entrada e mocks
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        DayOfMonth recurringDay = new DayOfMonth(1);

        IncomeRecurring incomeRecurring1 = IncomeRecurringFixture.umIncomeRecurringComId(1L, user, category,
                recurringDay, period);
        IncomeRecurring incomeRecurring2 = IncomeRecurringFixture.umIncomeRecurringComId(2L, user, category,
                recurringDay, period);

        ListIncomeRecurringsFilter filter = new ListIncomeRecurringsFilter();
        filter.setSearchString("teste");

        Pageable pageable = mock(Pageable.class);
        Page<IncomeRecurring> expectedPage = new PageImpl<>(Arrays.asList(incomeRecurring1, incomeRecurring2));

        when(incomeRecurringRepository.findAllBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When - Executar o método testado
        Page<IncomeRecurring> result = listIncomeRecurringsCase.execute(user, filter, pageable);

        // Then - Verificar os resultados
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(incomeRecurring1.getId(), result.getContent().get(0).getId());
        assertEquals(incomeRecurring2.getId(), result.getContent().get(1).getId());
        assertEquals(incomeRecurring1.getDescription(), result.getContent().get(0).getDescription());
        assertEquals(incomeRecurring2.getDescription(), result.getContent().get(1).getDescription());

        verify(incomeRecurringRepository).findAllBy(user.getId(), filter, pageable);
        verifyNoMoreInteractions(incomeRecurringRepository);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há receitas recorrentes")
    void listIncomeRecurrings_deveRetornarPaginaVazia_quandoNaoHaReceitas() {
        // Given - Preparar os dados de entrada e mocks
        User user = UserFixture.umUsuarioComId(1L);
        ListIncomeRecurringsFilter filter = new ListIncomeRecurringsFilter();
        filter.setSearchString("busca_sem_resultado");

        Pageable pageable = mock(Pageable.class);
        Page<IncomeRecurring> expectedPage = new PageImpl<>(Collections.emptyList());

        when(incomeRecurringRepository.findAllBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When - Executar o método testado
        Page<IncomeRecurring> result = listIncomeRecurringsCase.execute(user, filter, pageable);

        // Then - Verificar os resultados
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(incomeRecurringRepository).findAllBy(user.getId(), filter, pageable);
        verifyNoMoreInteractions(incomeRecurringRepository);
    }

    @Test
    @DisplayName("Deve funcionar corretamente com filtro vazio")
    void listIncomeRecurrings_deveFuncionarCorretamente_comFiltroVazio() {
        // Given - Preparar os dados de entrada e mocks
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        Period period = new Period(LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        DayOfMonth recurringDay = new DayOfMonth(1);

        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(1L, user, category,
                recurringDay, period);

        ListIncomeRecurringsFilter filter = new ListIncomeRecurringsFilter();
        // searchString fica null por padrão

        Pageable pageable = mock(Pageable.class);
        Page<IncomeRecurring> expectedPage = new PageImpl<>(Arrays.asList(incomeRecurring));

        when(incomeRecurringRepository.findAllBy(user.getId(), filter, pageable)).thenReturn(expectedPage);

        // When - Executar o método testado
        Page<IncomeRecurring> result = listIncomeRecurringsCase.execute(user, filter, pageable);

        // Then - Verificar os resultados
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(incomeRecurring.getId(), result.getContent().get(0).getId());
        assertEquals(incomeRecurring.getDescription(), result.getContent().get(0).getDescription());
        assertEquals(incomeRecurring.getValue(), result.getContent().get(0).getValue());
        assertEquals(incomeRecurring.getCategory().getId(), result.getContent().get(0).getCategory().getId());
        assertEquals(incomeRecurring.getUserId(), result.getContent().get(0).getUserId());

        verify(incomeRecurringRepository).findAllBy(user.getId(), filter, pageable);
        verifyNoMoreInteractions(incomeRecurringRepository);
    }
}
