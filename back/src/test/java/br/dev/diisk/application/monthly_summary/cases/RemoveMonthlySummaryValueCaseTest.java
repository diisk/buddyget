package br.dev.diisk.application.monthly_summary.cases;

import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.monthly_summary.dtos.GetMonthlySummaryParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para RemoveMonthlySummaryValueCase.
 * Segue padrão Given-When-Then e cobre cenários felizes, exceções e limites.
 */
@ExtendWith(MockitoExtension.class)
class RemoveMonthlySummaryValueCaseTest {

    @Mock
    private IMonthlySummaryRepository monthlySummaryRepository;
    @Mock
    private GetMonthlySummaryCase getMonthlySummaryCase;
    @Mock
    private User user;
    @Mock
    private Category category;
    @Mock
    private MonthlySummary monthlySummary;
    @InjectMocks
    private RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;

    private AddMonthlySummaryValueParams params;

    @BeforeEach
    void setUp() {
        params = mock(AddMonthlySummaryValueParams.class);
        when(params.getMonth()).thenReturn(6);
        when(params.getYear()).thenReturn(2025);
        when(params.getCategory()).thenReturn(category);
        when(params.getValue()).thenReturn(BigDecimal.TEN);
        lenient().when(category.getId()).thenReturn(1L);
    }

    @Test
    @DisplayName("removeMonthlySummaryValue_deveRemoverValor_quandoResumoMensalExiste")
    // Testa o caminho feliz: valor removido de um resumo mensal existente
    void removeMonthlySummaryValue_deveRemoverValor_quandoResumoMensalExiste() {
        // Given
        when(getMonthlySummaryCase.execute(any(), any(GetMonthlySummaryParams.class))).thenReturn(monthlySummary);
        // When
        MonthlySummary result = removeMonthlySummaryValueCase.execute(user, params);
        // Then
        assertNotNull(result);
        verify(monthlySummary).removeAmount(BigDecimal.TEN);
        verify(monthlySummaryRepository).save(monthlySummary);
    }

    @Test
    @DisplayName("removeMonthlySummaryValue_deveLancarExcecao_quandoCategoriaNula")
    // Testa exceção quando a categoria é nula
    void removeMonthlySummaryValue_deveLancarExcecao_quandoCategoriaNula() {
        // Given
        when(params.getCategory()).thenReturn(null);
        // When / Then
        NullOrEmptyException ex = assertThrows(NullOrEmptyException.class, () -> removeMonthlySummaryValueCase.execute(user, params));
        assertTrue(ex.getDetails().get("campo").equals("category"));
    }

    @Test
    @DisplayName("removeMonthlySummaryValue_deveLancarExcecao_quandoResumoMensalNaoExiste")
    // Testa exceção quando o resumo mensal não existe (está zerado)
    void removeMonthlySummaryValue_deveLancarExcecao_quandoResumoMensalNaoExiste() {
        // Given
        when(getMonthlySummaryCase.execute(any(), any(GetMonthlySummaryParams.class))).thenReturn(null);
        // When / Then
        BusinessException ex = assertThrows(BusinessException.class, () -> removeMonthlySummaryValueCase.execute(user, params));
        assertEquals("O resumo mensal alvo está zerado.", ex.getMessage());
    }

    @Test
    @DisplayName("removeMonthlySummaryValue_deveRemoverValor_quandoValorNegativoOuZero")
    // Testa remoção mesmo com valor zero ou negativo (regra delegada ao MonthlySummary)
    void removeMonthlySummaryValue_deveRemoverValor_quandoValorNegativoOuZero() {
        // Given
        when(getMonthlySummaryCase.execute(any(), any(GetMonthlySummaryParams.class))).thenReturn(monthlySummary);
        when(params.getValue()).thenReturn(BigDecimal.ZERO);
        // When
        MonthlySummary result = removeMonthlySummaryValueCase.execute(user, params);
        // Then
        assertNotNull(result);
        verify(monthlySummary).removeAmount(BigDecimal.ZERO);
        verify(monthlySummaryRepository).save(monthlySummary);
    }
}
