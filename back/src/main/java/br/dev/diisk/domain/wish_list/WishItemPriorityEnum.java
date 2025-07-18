package br.dev.diisk.domain.wish_list;

import br.dev.diisk.domain.shared.interfaces.IBaseEnum;

public enum WishItemPriorityEnum implements IBaseEnum {
    HIGH("Alta"),
    MEDIUM("Média"),
    LOW("Baixa"),
    ;

    private String description;

    WishItemPriorityEnum(String title) {
        this.description = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
