package br.dev.diisk.domain.entities.notification;


import br.dev.diisk.domain.entities.UserRastrableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
// @Inheritance(strategy = InheritanceType.JOINED)
//REVER ESSA ENTIDADE POIS SERÁ UM TIPO DE HERANÇA DIFERENTE
public class Notification extends UserRastrableEntity {

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean readed;

}
