package br.dev.diisk.domain.shared;

import br.dev.diisk.domain.shared.interfaces.IBaseEnum;
import lombok.Getter;

@Getter
public enum ErrorTypeEnum implements IBaseEnum {
    DOMAIN_BUSINESS("Erro de negócio"),
    CONFLICT("Conflito"),
    ACCESS_DENIED("Acesso negado"),
    UNAUTHORIZED("Sem autorização"),
    NOT_FOUND("Valor não encontrado");

    private final String description;

    ErrorTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
