package br.dev.diisk.domain.entities.user;

import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.dev.diisk.domain.entities.RastreableEntity;
import br.dev.diisk.domain.enums.user.UserPermissionEnum;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_perfils")
public class UserPerfil extends RastreableEntity{

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer level;

    @ElementCollection(targetClass = UserPermissionEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_perfil_permissions", joinColumns = @JoinColumn(name = "perfil_id"))
    @Column(name = "permission")
    @Fetch(FetchMode.JOIN)
    private List<UserPermissionEnum> permissions;

}
