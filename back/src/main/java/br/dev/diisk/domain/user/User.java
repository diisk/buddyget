package br.dev.diisk.domain.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.dev.diisk.domain.shared.entities.RastreableEntity;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User extends RastreableEntity implements UserDetails {

    @Column(nullable = false)
    private String name;

    @Embedded
    private Email email;

    @Column(nullable = false)
    private String encryptedPassword;

    @ManyToOne(optional = false)
    @JoinColumn(name = "perfil_id")
    private UserPerfil perfil;

    public User(String name, Email email, String encryptedPassword, UserPerfil perfil) {
        this.name = name;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.perfil = perfil;
        validate();
    }

    public Boolean update(String name, String encryptedPassword) {
        Boolean updated = false;
        if (name != null) {
            this.name = name;
            updated = true;
        }
        if (encryptedPassword != null) {
            this.encryptedPassword = encryptedPassword;
            updated = true;
        }
        validate();
        return updated;

    }

    public String getEmail() {
        return email.getValue();
    }

    private void validate() {
        validateName();
        validateEncryptedPassword();
    }

    private void validateEncryptedPassword() {
        if (encryptedPassword == null || encryptedPassword.isBlank())
            throw new NullOrEmptyException(getClass(), "encryptedPassword");
    }

    private void validateName() {
        if (name == null || name.isBlank())
            throw new NullOrEmptyException(getClass(), "name");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfil.getPermissions();
    }

    @Override
    public String getUsername() {
        return email.getValue();
    }

    @Override
    public String getPassword() {
        return encryptedPassword;
    }
}
