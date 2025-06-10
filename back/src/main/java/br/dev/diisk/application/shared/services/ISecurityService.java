package br.dev.diisk.application.shared.services;

import br.dev.diisk.domain.shared.value_objects.Password;

public interface ISecurityService {

    public String encryptPassword(Password password);

    public String encryptPassword(String password);

    public Boolean matchPasswords(String password, String encryptedPassword);

}
