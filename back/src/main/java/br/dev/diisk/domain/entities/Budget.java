package br.dev.diisk.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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
@Table(name = "budget")
public class Budget extends UserRastrableEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long limitValue;

    @Column(nullable = false)
    private Boolean active;

    @OneToOne(optional = false)
    private Category category;

}
