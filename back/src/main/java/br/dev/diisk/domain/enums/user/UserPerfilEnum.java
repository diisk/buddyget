package br.dev.diisk.domain.enums.user;

import br.dev.diisk.domain.interfaces.IBaseEnum;

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
