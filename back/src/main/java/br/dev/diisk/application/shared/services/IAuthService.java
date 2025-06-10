package br.dev.diisk.application.shared.services;

import br.dev.diisk.domain.user.User;

public interface IAuthService {

    public User authenticate(String username, String password);

}
