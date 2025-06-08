package br.dev.diisk.domain.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Testes unitários para o objeto de valor Metadata.
 * Cada teste segue o padrão Given-When-Then e cobre cenários felizes, limites e inválidos.
 */
@ExtendWith(MockitoExtension.class)
class MetadataTest {

    // Teste: Deve criar metadata corretamente
    @Test
    void metadata_deveCriarMetadata_quandoHashMapValido() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("chave", "valor");
        // When
        Metadata metadata = new Metadata(map);
        // Then
        assertEquals(map, metadata.getData());
        // Metadata criada corretamente
    }

    // Teste: Deve considerar iguais metadatas com mesmo valor
    @Test
    void metadata_deveSerIgual_quandoValoresIguais() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        Metadata m1 = new Metadata(map);
        Metadata m2 = new Metadata(map);
        // When/Then
        assertEquals(m1, m2);
        // Objetos Metadata iguais
    }
}
