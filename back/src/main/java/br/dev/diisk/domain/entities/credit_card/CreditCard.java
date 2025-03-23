package br.dev.diisk.domain.entities.credit_card;

import java.math.BigDecimal;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "credit_cards")
public class CreditCard extends UserRastrableEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer billDueDay;

    @Column(nullable = false)
    private Integer billClosingDay;

    @Column(nullable = false)
    private BigDecimal cardLimit;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Boolean active = true;

}
