package br.dev.diisk.domain.enums.expense;

import br.dev.diisk.domain.interfaces.IBaseEnum;

public enum FixedExpenseTypeEnum implements IBaseEnum {
    UNDEFINED_TIME("enum.fixed-expense.undefined-time"),
    DEADLINE("enum.fixed-expense.deadline"),

    ;

    private String titlePath;

    FixedExpenseTypeEnum(String titlePath) {
        this.titlePath = titlePath;
    }

    @Override
    public String getTitlePath() {
        return titlePath;
    }

}
