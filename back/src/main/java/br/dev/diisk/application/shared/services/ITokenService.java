package br.dev.diisk.application.shared.services;

public interface ITokenService {

    public String generateToken(String subject);

    public String getSubject(String token);

}
