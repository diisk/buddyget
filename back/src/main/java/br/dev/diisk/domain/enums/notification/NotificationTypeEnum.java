package br.dev.diisk.domain.enums.notification;

import br.dev.diisk.domain.MessageUtils;

public enum NotificationTypeEnum {
    BUDGET_EXCEEDED("enum.notification.budget-exceeded"),

    ;

    private String title;

    NotificationTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return MessageUtils.getMessage(title);
    }

}
