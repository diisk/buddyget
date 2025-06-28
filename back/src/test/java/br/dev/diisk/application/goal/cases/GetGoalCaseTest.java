package br.dev.diisk.application.goal.cases;

import br.dev.diisk.domain.goal.Goal;
import br.dev.diisk.domain.goal.GoalFixture;
import br.dev.diisk.domain.goal.IGoalRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o caso de uso GetGoalCase.
 */
@ExtendWith(MockitoExtension.class)
class GetGoalCaseTest {

    @Mock
    private IGoalRepository goalRepository;

    @InjectMocks
    private GetGoalCase getGoalCase;

    // Teste para o caminho feliz: deve retornar a meta quando encontrada e pertence ao usuário
    @Test
    void getGoal_deveRetornarMeta_quandoEncontradaEUsuarioCorreto() {
        // Given
        Long goalId = 1L;
        Long userId = 10L;
        User user = UserFixture.umUsuarioComId(userId);
        Goal goal = GoalFixture.umGoalComId(goalId, user);
        when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));

        // When
        Goal result = getGoalCase.execute(user, goalId);

        // Then
        assertNotNull(result);
        assertEquals(goal, result);
        assertEquals(goalId, result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals("Meta Teste", result.getDescription());
        verify(goalRepository).findById(goalId);
    }

    // Teste para exceção: deve lançar DatabaseValueNotFoundException quando a meta não for encontrada
    @Test
    void getGoal_deveLancarExcecao_quandoMetaNaoEncontrada() {
        // Given
        Long goalId = 2L;
        Long userId = 20L;
        User user = UserFixture.umUsuarioComId(userId);
        when(goalRepository.findById(goalId)).thenReturn(Optional.empty());

        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getGoalCase.execute(user, goalId)
        );
        // Valida os detalhes da exceção (chave correta: "valor")
        assertNotNull(ex.getDetails());
        assertEquals(goalId.toString(), ex.getDetails().get("valor"));
        verify(goalRepository).findById(goalId);
    }

    // Teste para exceção: deve lançar DatabaseValueNotFoundException quando a meta retorna null
    @Test
    void getGoal_deveLancarExcecao_quandoMetaRetornaNulo() {
        // Given
        Long goalId = 3L;
        Long userId = 30L;
        User user = UserFixture.umUsuarioComId(userId);
        when(goalRepository.findById(goalId)).thenReturn(Optional.ofNullable(null));

        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getGoalCase.execute(user, goalId)
        );
        // Valida os detalhes da exceção (chave correta: "valor")
        assertNotNull(ex.getDetails());
        assertEquals(goalId.toString(), ex.getDetails().get("valor"));
        verify(goalRepository).findById(goalId);
    }

    // Teste para exceção: deve lançar DatabaseValueNotFoundException quando a meta não pertence ao usuário
    @Test
    void getGoal_deveLancarExcecao_quandoMetaNaoPertenceAoUsuario() {
        // Given
        Long goalId = 4L;
        Long userId = 40L;
        Long otherUserId = 99L;
        User user = UserFixture.umUsuarioComId(userId);
        User otherUser = UserFixture.umUsuarioComId(otherUserId);
        Goal goal = GoalFixture.umGoalComId(goalId, otherUser);
        when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));

        // When & Then
        DatabaseValueNotFoundException ex = assertThrows(DatabaseValueNotFoundException.class, () ->
                getGoalCase.execute(user, goalId)
        );
        // Valida os detalhes da exceção (chave correta: "valor")
        assertNotNull(ex.getDetails());
        assertEquals(goalId.toString(), ex.getDetails().get("valor"));
        verify(goalRepository).findById(goalId);
    }
}
