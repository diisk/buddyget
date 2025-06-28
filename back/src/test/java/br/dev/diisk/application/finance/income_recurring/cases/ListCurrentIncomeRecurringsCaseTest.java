package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
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
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso ListCurrentIncomeRecurringsCase.
 * Testa todos os cenários relevantes para listagem de receitas recorrentes
 * ativas.
 */
@ExtendWith(MockitoExtension.class)
class ListCurrentIncomeRecurringsCaseTest {

    @Mock
    private IIncomeRecurringRepository incomeRecurringRepository;

    @InjectMocks
    private ListCurrentIncomeRecurringsCase listCurrentIncomeRecurringsCase;

    @Test
    @DisplayName("Deve listar receitas recorrentes ativas corretamente quando há resultados")
    void listCurrentIncomeRecurrings_deveListarCorretamente_quandoHaReceitasAtivas() {
        // Given - Preparar os dados de entrada e mocks
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        DataRange period = new DataRange(LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        DayOfMonth recurringDay = new DayOfMonth(1);

        IncomeRecurring incomeRecurring1 = IncomeRecurringFixture.umIncomeRecurringComId(1L, user, category,
                recurringDay, period);
        IncomeRecurring incomeRecurring2 = IncomeRecurringFixture.umIncomeRecurringComId(2L, user, category,
                recurringDay, period);
        IncomeRecurring incomeRecurring3 = IncomeRecurringFixture.umIncomeRecurringComId(3L, user, category,
                recurringDay, period);

        Set<IncomeRecurring> expectedIncomeRecurrings = Set.of(incomeRecurring1, incomeRecurring2, incomeRecurring3);

        when(incomeRecurringRepository.findAllActive()).thenReturn(expectedIncomeRecurrings);

        // When - Executar o método testado
        Set<IncomeRecurring> result = listCurrentIncomeRecurringsCase.execute();

        // Then - Verificar os resultados
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(incomeRecurring1));
        assertTrue(result.contains(incomeRecurring2));
        assertTrue(result.contains(incomeRecurring3));

        // Verificar que cada receita retornada possui os valores corretos
        for (IncomeRecurring incomeRecurring : result) {
            assertNotNull(incomeRecurring.getId());
            assertNotNull(incomeRecurring.getDescription());
            assertNotNull(incomeRecurring.getValue());
            assertNotNull(incomeRecurring.getCategory());
            assertNotNull(incomeRecurring.getUserId());
            assertEquals(CategoryTypeEnum.INCOME, incomeRecurring.getCategory().getType());
        }

        verify(incomeRecurringRepository).findAllActive();
        verifyNoMoreInteractions(incomeRecurringRepository);
    }

    @Test
    @DisplayName("Deve retornar conjunto vazio quando não há receitas recorrentes ativas")
    void listCurrentIncomeRecurrings_deveRetornarConjuntoVazio_quandoNaoHaReceitasAtivas() {
        // Given - Preparar mock para retornar conjunto vazio
        Set<IncomeRecurring> expectedIncomeRecurrings = Collections.emptySet();

        when(incomeRecurringRepository.findAllActive()).thenReturn(expectedIncomeRecurrings);

        // When - Executar o método testado
        Set<IncomeRecurring> result = listCurrentIncomeRecurringsCase.execute();

        // Then - Verificar os resultados
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(incomeRecurringRepository).findAllActive();
        verifyNoMoreInteractions(incomeRecurringRepository);
    }

    @Test
    @DisplayName("Deve retornar uma única receita recorrente ativa corretamente")
    void listCurrentIncomeRecurrings_deveRetornarUmaReceita_quandoHaApenasUmaReceitaAtiva() {
        // Given - Preparar uma única receita recorrente
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        DataRange period = new DataRange(LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        DayOfMonth recurringDay = new DayOfMonth(1);

        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(1L, user, category,
                recurringDay, period);
        Set<IncomeRecurring> expectedIncomeRecurrings = Set.of(incomeRecurring);

        when(incomeRecurringRepository.findAllActive()).thenReturn(expectedIncomeRecurrings);

        // When - Executar o método testado
        Set<IncomeRecurring> result = listCurrentIncomeRecurringsCase.execute();

        // Then - Verificar os resultados
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(incomeRecurring));

        IncomeRecurring resultIncomeRecurring = result.iterator().next();
        assertEquals(incomeRecurring.getId(), resultIncomeRecurring.getId());
        assertEquals(incomeRecurring.getDescription(), resultIncomeRecurring.getDescription());
        assertEquals(incomeRecurring.getValue(), resultIncomeRecurring.getValue());
        assertEquals(incomeRecurring.getCategory().getId(), resultIncomeRecurring.getCategory().getId());
        assertEquals(incomeRecurring.getUserId(), resultIncomeRecurring.getUserId());
        assertEquals(incomeRecurring.getPeriod().getStartDate(), resultIncomeRecurring.getPeriod().getStartDate());
        assertEquals(incomeRecurring.getPeriod().getEndDate(), resultIncomeRecurring.getPeriod().getEndDate());

        verify(incomeRecurringRepository).findAllActive();
        verifyNoMoreInteractions(incomeRecurringRepository);
    }

    @Test
    @DisplayName("Deve chamar o repositório corretamente independente do resultado")
    void listCurrentIncomeRecurrings_deveChamarRepositorio_independenteDoResultado() {
        // Given - Preparar mock para retornar qualquer resultado
        Set<IncomeRecurring> expectedIncomeRecurrings = Collections.emptySet();

        when(incomeRecurringRepository.findAllActive()).thenReturn(expectedIncomeRecurrings);

        // When - Executar o método testado
        listCurrentIncomeRecurringsCase.execute();

        // Then - Verificar que o repositório foi chamado corretamente
        verify(incomeRecurringRepository, times(1)).findAllActive();
        verifyNoMoreInteractions(incomeRecurringRepository);
    }
}
