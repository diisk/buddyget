package br.dev.diisk.domain.notification;

import br.dev.diisk.domain.shared.interfaces.IBaseEnum;

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
