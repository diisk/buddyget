package br.dev.diisk.domain.user.enums;

import br.dev.diisk.domain.shared.interfaces.IBaseEnum;

public enum UserPerfilEnum implements IBaseEnum {
    DEFAULT("Padrão"),

    ;

    private String description;

    UserPerfilEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
