package br.dev.diisk.domain.enums.notification;

import br.dev.diisk.domain.interfaces.IBaseEnum;

public enum NotificationTypeEnum implements IBaseEnum{
    BUDGET_EXCEEDED("Orçamento Excedido"),

    ;

    private String description;

    NotificationTypeEnum(String title) {
        this.description = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
