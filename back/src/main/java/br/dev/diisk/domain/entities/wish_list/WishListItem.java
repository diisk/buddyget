package br.dev.diisk.domain.entities.wish_list;

import java.math.BigDecimal;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.enums.wish_list.WishListItemStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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
@Table(name = "wish_list_items")
public class WishListItem extends UserRastrableEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String notes;

    @Column(nullable = true)
    private String link;

    @Column(nullable = false)
    private BigDecimal estimatedValue;

    @OneToOne(optional = true)
    private Expense expense;

    @ManyToOne(optional = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    private WishListItemStatusEnum status;

}
