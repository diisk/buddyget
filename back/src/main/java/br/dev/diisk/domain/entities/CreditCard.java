package br.dev.diisk.domain.entities;

import java.time.LocalDateTime;

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
    private Integer bestDay;

    @Column(nullable = false)
    private Long cardLimit;

    @Column(nullable = false)
    private LocalDateTime billDueDate;// DATA DE VENCIMENTO DA FATURA

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Boolean active;

}
