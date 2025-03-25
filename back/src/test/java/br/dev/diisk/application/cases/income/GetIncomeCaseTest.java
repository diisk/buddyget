package br.dev.diisk.application.cases.income;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.income.IIncomeRepository;

public class GetIncomeCaseTest {

    @Mock
    private IIncomeRepository incomeRepository;

    @InjectMocks
    private GetIncomeCase getIncomeCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getIncomeCase_quandoRendaExiste_DeveRetornarRenda() {
        // Given
        Long userId = 1L;
        Long incomeId = 1L;
        User user = new User();
        user.setId(userId);
        Income income = new Income();
        income.setId(incomeId);
        income.setUser(user);

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(income));

        // When
        Income result = getIncomeCase.execute(user, incomeId);

        // Then
        assertEquals(income, result);
    }

    @Test
    public void getIncomeCase_quandoRendaNaoExiste_DeveLancarDbValueNotFoundException() {
        // Given
        Long userId = 1L;
        Long incomeId = 1L;
        User user = new User();
        user.setId(userId);

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> getIncomeCase.execute(user, incomeId));
    }

    @Test
    public void getIncomeCase_quandoRendaNaoPertenceAoUsuario_DeveLancarDbValueNotFoundException() {
        // Given
        Long userId = 1L;
        Long incomeId = 1L;
        User user = new User();
        user.setId(userId);
        User anotherUser = new User();
        anotherUser.setId(2L);
        Income income = new Income();
        income.setId(incomeId);
        income.setUser(anotherUser);

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(income));

        // When / Then
        assertThrows(DbValueNotFoundException.class, () -> getIncomeCase.execute(user, incomeId));
    }
}
