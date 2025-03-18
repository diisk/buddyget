package br.dev.diisk.domain.enums.wish_list;

import br.dev.diisk.domain.MessageUtils;

public enum WishListItemStatusEnum {
    ACQUIRED("enum.wish-list-item.acquired"),
    DROPPED("enum.wish-list-item.dropped"),
    LOW_PRIORITY("enum.wish-list-item.low-priority"),
    PENDING("enum.wish-list-item.pending"),
    ;

    private String title;

    WishListItemStatusEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return MessageUtils.getMessage(title);
    }

}
