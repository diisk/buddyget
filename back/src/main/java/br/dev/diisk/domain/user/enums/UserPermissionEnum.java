package br.dev.diisk.domain.user.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserPermissionEnum implements GrantedAuthority {
    DEFAULT("Padrão"),

    ;

    private String title;

    UserPermissionEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getAuthority() {
        return toString();
    }
}
