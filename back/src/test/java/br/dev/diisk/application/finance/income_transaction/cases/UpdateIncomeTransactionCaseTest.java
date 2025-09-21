package br.dev.diisk.application.finance.income_transaction.cases;

import br.dev.diisk.application.finance.income_transaction.dtos.UpdateIncomeTransactionParams;
import br.dev.diisk.application.monthly_summary.cases.UpdateMonthlySummaryCase;
import br.dev.diisk.application.monthly_summary.dtos.UpdateMonthlySummaryParams;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para UpdateIncomeTransactionCase.
 * Segue o padrão de nomeação, Given-When-Then e valida todos os fluxos da
 * classe.
 */
@ExtendWith(MockitoExtension.class)
class UpdateIncomeTransactionCaseTest {

    @Mock
    private IIncomeTransactionRepository incomeRepository;
    @Mock
    private GetIncomeTransactionCase getIncomeTransactionCase;
    @Mock
    private UpdateMonthlySummaryCase updateMonthlySummaryCase;

    @InjectMocks
    private UpdateIncomeTransactionCase updateIncomeTransactionCase;

    // Fixture para IncomeTransaction
    private IncomeTransaction umaIncomeTransactionComValores(User user, Long id, BigDecimal valor, LocalDateTime data,
            Category categoria) {
        IncomeTransaction tx = mock(IncomeTransaction.class);
        when(tx.getValue()).thenReturn(valor);
        when(tx.getPaymentDate()).thenReturn(data);
        when(tx.getCategory()).thenReturn(categoria);
        return tx;
    }

    // Fixture para User
    private User umUsuarioComId(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    // Fixture para Category
    private Category umaCategoria(Long id, User user) {
        Category c = new Category(user, "Categoria Teste", "desc", "icon", CategoryTypeEnum.INCOME,
                new HexadecimalColor("#123456"));
        c.setId(id);
        return c;
    }

    @Test
    void updateIncomeTransaction_deveAtualizarCorretamente_quandoParametrosValidos() {
        // Testa o caminho feliz: atualização bem-sucedida
        // Given
        User user = umUsuarioComId(1L);
        Long txId = 10L;
        BigDecimal valorAntigo = new BigDecimal("100.00");
        LocalDateTime dataAntiga = LocalDateTime.of(2024, 1, 1, 0, 0);
        Category categoria = umaCategoria(5L, user);
        IncomeTransaction tx = umaIncomeTransactionComValores(user, txId, valorAntigo, dataAntiga, categoria);
        UpdateIncomeTransactionParams params = mock(UpdateIncomeTransactionParams.class);
        when(params.getDescription()).thenReturn("Nova descrição");
        when(params.getValue()).thenReturn(new BigDecimal("200.00"));
        when(params.getPaymentDate()).thenReturn(LocalDateTime.of(2024, 2, 1, 0, 0));
        when(getIncomeTransactionCase.execute(user, txId)).thenReturn(tx);
        // Simula update: após update, getValue/getReceiptDate retornam novos valores
        doAnswer(invocation -> {
            when(tx.getValue()).thenReturn(new BigDecimal("200.00"));
            when(tx.getPaymentDate()).thenReturn(LocalDateTime.of(2024, 2, 1, 0, 0));
            return null;
        }).when(tx).update(anyString(), any(BigDecimal.class), any(LocalDateTime.class));

        // When
        IncomeTransaction resultado = updateIncomeTransactionCase.execute(user, txId, params);

        // Then
        assertEquals(tx, resultado); // Deve retornar a mesma instância
        verify(tx).update("Nova descrição", new BigDecimal("200.00"), LocalDateTime.of(2024, 2, 1, 0, 0));
        verify(incomeRepository).save(tx);
        // Captura os parâmetros enviados para o resumo mensal
        ArgumentCaptor<UpdateMonthlySummaryParams> captor = ArgumentCaptor.forClass(UpdateMonthlySummaryParams.class);
        verify(updateMonthlySummaryCase).execute(eq(user), captor.capture());
        UpdateMonthlySummaryParams summaryParams = captor.getValue();
        assertEquals(valorAntigo, summaryParams.getPreviousValue());
        assertEquals(dataAntiga, summaryParams.getPreviousDate());
        assertEquals(new BigDecimal("200.00"), summaryParams.getNewValue());
        assertEquals(LocalDateTime.of(2024, 2, 1, 0, 0), summaryParams.getNewDate());
        assertEquals(categoria, summaryParams.getCategory());
    }
}
