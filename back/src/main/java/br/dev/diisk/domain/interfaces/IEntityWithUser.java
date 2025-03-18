package br.dev.diisk.domain.interfaces;

import br.dev.diisk.domain.entities.user.User;

public interface IEntityWithUser {

    User getUser();

    void setUser(User user);
}
