package br.dev.diisk.domain.enums;

import br.dev.diisk.domain.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
public enum ExceptionTypeEnum implements IBaseEnum {
    DOMAIN_BUSINESS("Erro de negócio"),
    NULL_VALUE("Valor nulo"),
    CONFLICT("Conflito"),
    VALUE_NOT_FOUND("Valor não encontrado");

    private final String description;

    ExceptionTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
