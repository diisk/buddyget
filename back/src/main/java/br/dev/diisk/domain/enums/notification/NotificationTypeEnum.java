package br.dev.diisk.domain.enums.notification;

import br.dev.diisk.domain.interfaces.IBaseEnum;

public enum NotificationTypeEnum implements IBaseEnum{
    BUDGET_EXCEEDED("Orçamento Excedido"),

    ;

    private String titlePath;

    NotificationTypeEnum(String title) {
        this.titlePath = title;
    }

    @Override
    public String getTitlePath() {
        return titlePath;
    }

}
