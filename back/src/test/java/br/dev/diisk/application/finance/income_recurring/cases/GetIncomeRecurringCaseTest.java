package br.dev.diisk.application.finance.income_recurring.cases;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurringFixture;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para GetIncomeRecurringCase.
 */
@ExtendWith(MockitoExtension.class)
class GetIncomeRecurringCaseTest {

    @Mock
    private IIncomeRecurringRepository incomeRecurringRepository;

    @InjectMocks
    private GetIncomeRecurringCase getIncomeRecurringCase;

    @Test
    @DisplayName("Deve retornar a receita recorrente corretamente quando encontrada e pertence ao usuário")
    void getIncomeRecurring_deveRetornarReceitaRecorrente_quandoEncontradaEPertenceAoUsuario() {
        // Given - Prepara os dados de entrada e mocks
        Long userId = 10L;
        Long incomeRecurringId = 1L;
        User user = UserFixture.umUsuarioComId(userId);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        DataRange period = new DataRange(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(incomeRecurringId, user,
                category, recurringDay, period);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));

        // When - Executa o método a ser testado
        IncomeRecurring result = getIncomeRecurringCase.execute(user, incomeRecurringId);

        // Then - Verifica os resultados usando assertions
        assertNotNull(result);
        assertEquals(incomeRecurring, result);
        assertEquals(incomeRecurringId, result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals("Receita Recorrente Teste", result.getDescription());
        verify(incomeRecurringRepository).findById(incomeRecurringId);
    }

    @Test
    @DisplayName("Deve lançar DatabaseValueNotFoundException quando a receita recorrente não for encontrada")
    void getIncomeRecurring_deveLancarExcecao_quandoReceitaRecorrenteNaoEncontrada() {
        // Given - Prepara o cenário onde a receita recorrente não existe
        Long userId = 10L;
        Long incomeRecurringId = 1L;
        User user = UserFixture.umUsuarioComId(userId);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.empty());

        // When / Then - Executa e verifica se a exceção é lançada
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class,
                () -> getIncomeRecurringCase.execute(user, incomeRecurringId));

        // Comentário: Verifica se a exceção é lançada quando a receita recorrente não
        // existe
        assertEquals(incomeRecurringId.toString(), ex.getDetails().get("valor"));
        verify(incomeRecurringRepository).findById(incomeRecurringId);
    }

    @Test
    @DisplayName("Deve lançar DatabaseValueNotFoundException quando a receita recorrente não pertence ao usuário")
    void getIncomeRecurring_deveLancarExcecao_quandoReceitaRecorrenteNaoPertenceAoUsuario() {
        // Given - Prepara o cenário onde a receita recorrente pertence a outro usuário
        Long userId = 10L;
        Long outroUserId = 99L;
        Long incomeRecurringId = 1L;
        User user = UserFixture.umUsuarioComId(userId);
        User outroUser = UserFixture.umUsuarioComId(outroUserId);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, outroUser);
        DataRange period = new DataRange(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        DayOfMonth recurringDay = new DayOfMonth(1);
        IncomeRecurring incomeRecurring = IncomeRecurringFixture.umIncomeRecurringComId(incomeRecurringId, outroUser,
                category, recurringDay, period);

        when(incomeRecurringRepository.findById(incomeRecurringId)).thenReturn(Optional.of(incomeRecurring));

        // When / Then - Executa e verifica se a exceção é lançada
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class,
                () -> getIncomeRecurringCase.execute(user, incomeRecurringId));

        // Comentário: Verifica se a exceção é lançada quando a receita recorrente não
        // pertence ao usuário
        assertEquals(incomeRecurringId.toString(), ex.getDetails().get("valor"));
        verify(incomeRecurringRepository).findById(incomeRecurringId);
    }

}
