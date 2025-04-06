package br.dev.diisk.domain.entities.expense;

import java.math.BigDecimal;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class GenericExpense extends UserRastrableEntity {

    @Column(nullable = false)
    private String description;

    @ManyToOne(optional = false)
    private Category category;

    @ManyToOne(optional = true)
    private CreditCard creditCard;

    @Column(nullable = false)
    private BigDecimal amount;
    

}
