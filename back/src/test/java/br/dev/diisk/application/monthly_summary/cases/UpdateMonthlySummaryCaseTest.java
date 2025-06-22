package br.dev.diisk.application.monthly_summary.cases;

import br.dev.diisk.application.monthly_summary.dtos.AddMonthlySummaryValueParams;
import br.dev.diisk.application.monthly_summary.dtos.RemoveMonthlySummaryValueParams;
import br.dev.diisk.application.monthly_summary.dtos.UpdateMonthlySummaryParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para UpdateMonthlySummaryCase.
 * Cada teste segue o padrão Given-When-Then e cobre cenários principais e limites.
 */
@ExtendWith(MockitoExtension.class)
class UpdateMonthlySummaryCaseTest {

    @Mock
    private RemoveMonthlySummaryValueCase removeMonthlySummaryValueCase;
    @Mock
    private AddMonthlySummaryValueCase addMonthlySummaryValueCase;
    @Mock
    private Category category;
    @Mock
    private User user;
    @Mock
    private MonthlySummary monthlySummary;

    // Fixture para UpdateMonthlySummaryParams
    private UpdateMonthlySummaryParams buildParams(BigDecimal previousValue, LocalDateTime previousDate, BigDecimal newValue, LocalDateTime newDate, Category category) {
        UpdateMonthlySummaryParams params = mock(UpdateMonthlySummaryParams.class);
        when(params.getPreviousValue()).thenReturn(previousValue);
        when(params.getPreviousDate()).thenReturn(previousDate);
        when(params.getNewValue()).thenReturn(newValue);
        when(params.getNewDate()).thenReturn(newDate);
        when(params.getCategory()).thenReturn(category);
        return params;
    }

    /**
     * Deve remover o valor anterior e adicionar o novo quando ambos os valores e datas são diferentes.
     */
    @Test
    void updateMonthlySummary_deveRemoverEAdicionar_quandoValorEMesDiferentes() {
        // Given
        BigDecimal previousValue = new BigDecimal("100.00");
        LocalDateTime previousDate = LocalDateTime.of(2024, 5, 1, 0, 0);
        BigDecimal newValue = new BigDecimal("200.00");
        LocalDateTime newDate = LocalDateTime.of(2024, 6, 1, 0, 0);
        UpdateMonthlySummaryParams params = buildParams(previousValue, previousDate, newValue, newDate, category);
        when(removeMonthlySummaryValueCase.execute(eq(user), any(RemoveMonthlySummaryValueParams.class))).thenReturn(monthlySummary);
        when(addMonthlySummaryValueCase.execute(eq(user), any(AddMonthlySummaryValueParams.class))).thenReturn(monthlySummary);

        UpdateMonthlySummaryCase useCase = new UpdateMonthlySummaryCase(removeMonthlySummaryValueCase, addMonthlySummaryValueCase);

        // When
        Optional<MonthlySummary> result = useCase.execute(user, params);

        // Then
        assertTrue(result.isPresent());
        assertEquals(monthlySummary, result.get());
        verify(removeMonthlySummaryValueCase).execute(eq(user), any(RemoveMonthlySummaryValueParams.class));
        verify(addMonthlySummaryValueCase).execute(eq(user), any(AddMonthlySummaryValueParams.class));
    }

    /**
     * Deve apenas remover o valor anterior quando o novo valor é nulo.
     */
    @Test
    void updateMonthlySummary_deveRemoverApenas_quandoNovoValorNulo() {
        // Given
        BigDecimal previousValue = new BigDecimal("100.00");
        LocalDateTime previousDate = LocalDateTime.of(2024, 5, 1, 0, 0);
        BigDecimal newValue = new BigDecimal("100.00");
        LocalDateTime newDate = null;
        UpdateMonthlySummaryParams params = buildParams(previousValue, previousDate, newValue, newDate, category);
        when(removeMonthlySummaryValueCase.execute(eq(user), any(RemoveMonthlySummaryValueParams.class))).thenReturn(monthlySummary);

        UpdateMonthlySummaryCase useCase = new UpdateMonthlySummaryCase(removeMonthlySummaryValueCase, addMonthlySummaryValueCase);

        // When
        Optional<MonthlySummary> result = useCase.execute(user, params);

        // Then
        assertTrue(result.isPresent());
        assertEquals(monthlySummary, result.get());
        verify(removeMonthlySummaryValueCase).execute(eq(user), any(RemoveMonthlySummaryValueParams.class));
        verify(addMonthlySummaryValueCase, never()).execute(any(), any());
    }

    /**
     * Deve apenas adicionar o novo valor quando não havia valor anterior.
     */
    @Test
    void updateMonthlySummary_deveAdicionarApenas_quandoNaoHaviaValorAnterior() {
        // Given
        BigDecimal previousValue = new BigDecimal("0.00");
        LocalDateTime previousDate = null;
        BigDecimal newValue = new BigDecimal("150.00");
        LocalDateTime newDate = LocalDateTime.of(2024, 7, 1, 0, 0);
        UpdateMonthlySummaryParams params = buildParams(previousValue, previousDate, newValue, newDate, category);
        when(addMonthlySummaryValueCase.execute(eq(user), any(AddMonthlySummaryValueParams.class))).thenReturn(monthlySummary);

        UpdateMonthlySummaryCase useCase = new UpdateMonthlySummaryCase(removeMonthlySummaryValueCase, addMonthlySummaryValueCase);

        // When
        Optional<MonthlySummary> result = useCase.execute(user, params);

        // Then
        assertTrue(result.isPresent());
        assertEquals(monthlySummary, result.get());
        verify(removeMonthlySummaryValueCase, never()).execute(any(), any());
        verify(addMonthlySummaryValueCase).execute(eq(user), any(AddMonthlySummaryValueParams.class));
    }

    /**
     * Deve retornar Optional.empty quando não há remoção nem adição.
     */
    @Test
    void updateMonthlySummary_deveRetornarEmpty_quandoNadaParaRemoverOuAdicionar() {
        // Given
        BigDecimal previousValue = new BigDecimal("100.00");
        LocalDateTime previousDate = null;
        BigDecimal newValue = new BigDecimal("100.00");
        LocalDateTime newDate = null;
        UpdateMonthlySummaryParams params = buildParams(previousValue, previousDate, newValue, newDate, category);

        UpdateMonthlySummaryCase useCase = new UpdateMonthlySummaryCase(removeMonthlySummaryValueCase, addMonthlySummaryValueCase);

        // When
        Optional<MonthlySummary> result = useCase.execute(user, params);

        // Then
        assertTrue(result.isEmpty());
        verify(removeMonthlySummaryValueCase, never()).execute(any(), any());
        verify(addMonthlySummaryValueCase, never()).execute(any(), any());
    }

    /**
     * Deve remover e adicionar corretamente quando apenas o valor muda, mas a data permanece igual.
     */
    @Test
    void updateMonthlySummary_deveRemoverEAdicionar_quandoValorMudaEMesIgual() {
        // Given
        BigDecimal previousValue = new BigDecimal("100.00");
        LocalDateTime previousDate = LocalDateTime.of(2024, 5, 1, 0, 0);
        BigDecimal newValue = new BigDecimal("200.00");
        LocalDateTime newDate = LocalDateTime.of(2024, 5, 1, 0, 0);
        UpdateMonthlySummaryParams params = buildParams(previousValue, previousDate, newValue, newDate, category);
        when(removeMonthlySummaryValueCase.execute(eq(user), any(RemoveMonthlySummaryValueParams.class))).thenReturn(monthlySummary);
        when(addMonthlySummaryValueCase.execute(eq(user), any(AddMonthlySummaryValueParams.class))).thenReturn(monthlySummary);

        UpdateMonthlySummaryCase useCase = new UpdateMonthlySummaryCase(removeMonthlySummaryValueCase, addMonthlySummaryValueCase);

        // When
        Optional<MonthlySummary> result = useCase.execute(user, params);

        // Then
        assertTrue(result.isPresent());
        assertEquals(monthlySummary, result.get());
        verify(removeMonthlySummaryValueCase).execute(eq(user), any(RemoveMonthlySummaryValueParams.class));
        verify(addMonthlySummaryValueCase).execute(eq(user), any(AddMonthlySummaryValueParams.class));
    }
}
