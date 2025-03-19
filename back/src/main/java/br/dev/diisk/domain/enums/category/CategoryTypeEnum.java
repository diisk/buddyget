package br.dev.diisk.domain.enums.category;

import br.dev.diisk.domain.interfaces.IBaseEnum;

public enum CategoryTypeEnum implements IBaseEnum {
    INCOME("enum.category.income"),
    EXPENSE("enum.category.expense"),

    ;

    private String titlePath;

    CategoryTypeEnum(String titlePath) {
        this.titlePath = titlePath;
    }

    @Override
    public String getTitlePath() {
        return titlePath;
    }

}
