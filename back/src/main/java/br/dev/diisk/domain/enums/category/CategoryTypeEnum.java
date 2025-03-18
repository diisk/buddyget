package br.dev.diisk.domain.enums.category;

import br.dev.diisk.domain.MessageUtils;

public enum CategoryTypeEnum {
    INCOME("enum.category.income"),
    EXPENSE("enum.category.expense"),

    ;

    private String title;

    CategoryTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return MessageUtils.getMessage(title);
    }

}
