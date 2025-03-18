package br.dev.diisk.domain.enums.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserPermissionEnum implements GrantedAuthority {
    DEFAULT("enum.user-permission.default"),

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
