package br.dev.diisk.domain.enums.notification;

import br.dev.diisk.domain.interfaces.IBaseEnum;

public enum NotificationTypeEnum implements IBaseEnum{
    BUDGET_EXCEEDED("enum.notification.budget-exceeded"),

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
