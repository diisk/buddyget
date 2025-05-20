package br.dev.diisk.domain.enums.wish_list;

import br.dev.diisk.domain.interfaces.IBaseEnum;

public enum WishItemPriorityEnum implements IBaseEnum {
    HIGH("Alta"),
    MEDIUM("Média"),
    LOW("Baixa"),
    ;

    private String titlePath;

    WishItemPriorityEnum(String title) {
        this.titlePath = title;
    }

    @Override
    public String getTitlePath() {
        return titlePath;
    }

}
