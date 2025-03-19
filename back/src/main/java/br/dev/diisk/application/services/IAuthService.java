package br.dev.diisk.application.services;

import br.dev.diisk.domain.entities.user.User;

public interface IAuthService {

    public User authenticate(String username, String password);

}
