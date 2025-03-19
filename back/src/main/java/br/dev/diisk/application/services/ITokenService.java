package br.dev.diisk.application.services;

import br.dev.diisk.domain.entities.user.User;

public interface ITokenService {

    public String generateToken(User user);

    public String validateToken(String token);

}
