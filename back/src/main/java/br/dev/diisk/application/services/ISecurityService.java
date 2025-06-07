package br.dev.diisk.application.services;

import br.dev.diisk.domain.value_objects.Password;

public interface ISecurityService {

    public String encryptPassword(Password password);

    public String encryptPassword(String password);

    public Boolean matchPasswords(String password, String encryptedPassword);

}
