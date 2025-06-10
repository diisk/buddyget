package br.dev.diisk.domain.category;

import br.dev.diisk.domain.shared.interfaces.IBaseEnum;

public enum CategoryTypeEnum implements IBaseEnum {
    INCOME("Receita"),
    EXPENSE("Despesa"),

    ;

    private String description;

    CategoryTypeEnum(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
