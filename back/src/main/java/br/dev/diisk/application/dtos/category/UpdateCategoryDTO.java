package br.dev.diisk.application.dtos.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCategoryDTO {

        private String description;
        private Boolean active;
}
