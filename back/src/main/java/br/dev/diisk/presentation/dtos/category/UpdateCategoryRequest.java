package br.dev.diisk.presentation.dtos.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCategoryRequest {

        private String description;
        private Boolean active;
}
