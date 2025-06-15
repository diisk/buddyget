package br.dev.diisk.application.monthly_summary.cases;

import br.dev.diisk.application.budget.cases.GetBudgetCase;
import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.domain.budget.Budget;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AddMonthlySummaryValueCase.
 * Cada teste cobre um cenário relevante do caso de uso, seguindo o padrão Given-When-Then.
 */
@ExtendWith(MockitoExtension.class)
class AddMonthlySummaryValueCaseTest {
    @Mock
    private IMonthlySummaryRepository monthlySummaryRepository;
    @Mock
    private GetBudgetCase getBudgetCase;
    @Mock
    private GetMonthlySummaryCase getMonthlySummaryCase;
    @InjectMocks
    private AddMonthlySummaryValueCase addMonthlySummaryValueCase;

    private User user;
    private Category category;
    private AddMonthlySummaryValueParams params;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        category = mock(Category.class);
        params = new AddMonthlySummaryValueParams(6, 2025, new BigDecimal("100.00"), category);
    }

    @Test
    @DisplayName("Deve adicionar valor ao resumo mensal existente")
    void addMonthlySummaryValue_deveAdicionarValor_quandoResumoMensalExiste() {
        // Given: resumo mensal já existe
        MonthlySummary existingSummary = mock(MonthlySummary.class);
        when(category.getId()).thenReturn(1L); // stubbing necessário apenas aqui
        when(getMonthlySummaryCase.execute(any(), any())).thenReturn(existingSummary);

        // When
        addMonthlySummaryValueCase.execute(user, params);

        // Then: valor adicionado e salvo
        verify(existingSummary).addAmount(new BigDecimal("100.00"));
        verify(monthlySummaryRepository).save(existingSummary);
    }

    @Test
    @DisplayName("Deve criar novo resumo mensal se não existir e adicionar valor")
    void addMonthlySummaryValue_deveCriarNovoResumo_quandoResumoNaoExiste() {
        // Given: resumo mensal não existe
        when(category.getId()).thenReturn(1L); // stubbing necessário apenas aqui
        when(getMonthlySummaryCase.execute(any(), any())).thenReturn(null);
        Budget budget = mock(Budget.class);
        when(getBudgetCase.execute(user, category)).thenReturn(budget);
        when(budget.getLimitValue()).thenReturn(500L);

        // When
        addMonthlySummaryValueCase.execute(user, params);

        // Then: novo resumo criado, valor adicionado e salvo
        ArgumentCaptor<MonthlySummary> captor = ArgumentCaptor.forClass(MonthlySummary.class);
        verify(monthlySummaryRepository).save(captor.capture());
        MonthlySummary saved = captor.getValue();
        assertEquals(new BigDecimal("100.00"), saved.getAmount());
        assertEquals(6, saved.getMonth());
        assertEquals(2025, saved.getYear());
        assertEquals(500L, saved.getBudgetLimit());
        assertEquals(category, saved.getCategory());
    }

    @Test
    @DisplayName("Deve criar novo resumo com limite zero se não houver orçamento")
    void addMonthlySummaryValue_deveCriarResumoComLimiteZero_quandoNaoHaOrcamento() {
        // Given: resumo mensal não existe e não há orçamento
        when(category.getId()).thenReturn(1L); // stubbing necessário apenas aqui
        when(getMonthlySummaryCase.execute(any(), any())).thenReturn(null);
        when(getBudgetCase.execute(user, category)).thenReturn(null);

        // When
        addMonthlySummaryValueCase.execute(user, params);

        // Then: novo resumo criado com limite zero
        ArgumentCaptor<MonthlySummary> captor = ArgumentCaptor.forClass(MonthlySummary.class);
        verify(monthlySummaryRepository).save(captor.capture());
        MonthlySummary saved = captor.getValue();
        assertEquals(0L, saved.getBudgetLimit());
    }

    @Test
    @DisplayName("Deve lançar exceção se categoria for nula")
    void addMonthlySummaryValue_deveLancarExcecao_quandoCategoriaNula() {
        // Given: categoria nula
        params.setCategory(null);

        // When/Then: lança NullOrEmptyException
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () ->
                addMonthlySummaryValueCase.execute(user, params));
        assertTrue(ex.getDetails().get("campo").equals("category"));
    }
}
