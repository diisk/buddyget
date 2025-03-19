package br.dev.diisk.domain.enums.wish_list;

import br.dev.diisk.domain.interfaces.IBaseEnum;

public enum WishListItemStatusEnum implements IBaseEnum {
    ACQUIRED("enum.wish-list-item.acquired"),
    DROPPED("enum.wish-list-item.dropped"),
    LOW_PRIORITY("enum.wish-list-item.low-priority"),
    PENDING("enum.wish-list-item.pending"),
    ;

    private String titlePath;

    WishListItemStatusEnum(String title) {
        this.titlePath = title;
    }

    @Override
    public String getTitlePath() {
        return titlePath;
    }

}
