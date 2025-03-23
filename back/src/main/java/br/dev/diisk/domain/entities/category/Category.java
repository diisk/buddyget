package br.dev.diisk.domain.entities.category;

import br.dev.diisk.domain.entities.UserRastrableEntity;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends UserRastrableEntity{

    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    private CategoryTypeEnum type;

}
