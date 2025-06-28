package br.dev.diisk.application.monthly_summary.cases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.diisk.application.monthly_summary.dtos.GetMonthlySummaryParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.monthly_summary.IMonthlySummaryRepository;
import br.dev.diisk.domain.monthly_summary.MonthlySummary;
import br.dev.diisk.domain.monthly_summary.MonthlySummaryFixture;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

@ExtendWith(MockitoExtension.class)
class GetMonthlySummaryCaseTest {

    @Mock
    private IMonthlySummaryRepository monthlySummaryRepository;

    @InjectMocks
    private GetMonthlySummaryCase getMonthlySummaryCase;

    @Test
    void getMonthlySummary_deveRetornarMonthlySummary_quandoEncontradoNoBanco() {
        // Given - prepara os dados de teste e mocks
        Long userId = 1L;
        Integer month = 6;
        Integer year = 2025;
        Long categoryId = 1L;
        
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        MonthlySummary expectedMonthlySummary = MonthlySummaryFixture.umMonthlySummaryComId(1L, user, category, month, year);
        
        GetMonthlySummaryParams params = new GetMonthlySummaryParams(month, year, categoryId);
        
        when(monthlySummaryRepository.findBy(userId, month, year, categoryId))
            .thenReturn(Optional.of(expectedMonthlySummary));

        // When - executa o método a ser testado
        MonthlySummary result = getMonthlySummaryCase.execute(user, params);

        // Then - verifica os resultados
        assertEquals(expectedMonthlySummary, result);
        assertEquals(expectedMonthlySummary.getId(), result.getId());
        assertEquals(expectedMonthlySummary.getMonth(), result.getMonth());
        assertEquals(expectedMonthlySummary.getYear(), result.getYear());
        assertEquals(expectedMonthlySummary.getAmount(), result.getAmount());
        assertEquals(expectedMonthlySummary.getBudgetLimit(), result.getBudgetLimit());
        assertEquals(expectedMonthlySummary.getCategory(), result.getCategory());
        assertEquals(expectedMonthlySummary.getUserId(), result.getUserId());
        
        verify(monthlySummaryRepository).findBy(userId, month, year, categoryId);
    }

    @Test
    void getMonthlySummary_deveRetornarNull_quandoNaoEncontradoNoBanco() {
        // Given - prepara os dados de teste e mocks
        Long userId = 1L;
        Integer month = 6;
        Integer year = 2025;
        Long categoryId = 1L;
        
        User user = UserFixture.umUsuarioComId(userId);
        GetMonthlySummaryParams params = new GetMonthlySummaryParams(month, year, categoryId);
        
        when(monthlySummaryRepository.findBy(userId, month, year, categoryId))
            .thenReturn(Optional.empty());

        // When - executa o método a ser testado
        MonthlySummary result = getMonthlySummaryCase.execute(user, params);

        // Then - verifica os resultados
        assertNull(result);
        
        verify(monthlySummaryRepository).findBy(userId, month, year, categoryId);
    }

    @Test
    void getMonthlySummary_deveRetornarMonthlySummary_comParametrosLimites() {
        // Given - prepara os dados de teste com valores limites (janeiro e dezembro)
        Long userId = 1L;
        Integer month = 1; // Janeiro
        Integer year = 2023;
        Long categoryId = 999L;
        
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.EXPENSE, user);
        MonthlySummary expectedMonthlySummary = MonthlySummaryFixture.umMonthlySummaryComId(3L, user, category, month, year);
        
        GetMonthlySummaryParams params = new GetMonthlySummaryParams(month, year, categoryId);
        
        when(monthlySummaryRepository.findBy(userId, month, year, categoryId))
            .thenReturn(Optional.of(expectedMonthlySummary));

        // When - executa o método a ser testado
        MonthlySummary result = getMonthlySummaryCase.execute(user, params);

        // Then - verifica os resultados
        assertEquals(expectedMonthlySummary, result);
        assertEquals(month, result.getMonth());
        assertEquals(year, result.getYear());
        
        verify(monthlySummaryRepository).findBy(userId, month, year, categoryId);
    }

    @Test
    void getMonthlySummary_deveRetornarMonthlySummary_comMesDoze() {
        // Given - prepara os dados de teste com mês 12 (dezembro)
        Long userId = 2L;
        Integer month = 12; // Dezembro
        Integer year = 2025;
        Long categoryId = 5L;
        
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(categoryId, CategoryTypeEnum.INCOME, user);
        MonthlySummary expectedMonthlySummary = MonthlySummaryFixture.umMonthlySummaryComId(4L, user, category, month, year);
        
        GetMonthlySummaryParams params = new GetMonthlySummaryParams(month, year, categoryId);
        
        when(monthlySummaryRepository.findBy(userId, month, year, categoryId))
            .thenReturn(Optional.of(expectedMonthlySummary));

        // When - executa o método a ser testado
        MonthlySummary result = getMonthlySummaryCase.execute(user, params);

        // Then - verifica os resultados
        assertEquals(expectedMonthlySummary, result);
        assertEquals(month, result.getMonth());
        assertEquals(year, result.getYear());
        assertEquals(userId, result.getUserId());
        
        verify(monthlySummaryRepository).findBy(userId, month, year, categoryId);
    }
}
