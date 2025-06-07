package br.dev.diisk.application.services;

public interface ITokenService {

    public String generateToken(String subject);

    public String getSubject(String token);

}
