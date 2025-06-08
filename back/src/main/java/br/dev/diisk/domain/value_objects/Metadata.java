package br.dev.diisk.domain.value_objects;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Metadata {

    @Column(name = "metadata", nullable = true, unique = false)
    private String value;

    public Metadata(Map<String, String> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.value = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting HashMap to JSON string", e);
        }
    }

    public Map<String, String> getData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(value, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON string to HashMap", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Metadata metadata = (Metadata) obj;
        return value.equals(metadata.value);
    }

    @Override
    public String toString() {
        return value;
    }
}