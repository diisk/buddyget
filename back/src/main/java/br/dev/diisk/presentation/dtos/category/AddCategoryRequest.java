package br.dev.diisk.presentation.dtos.category;

import br.dev.diisk.domain.ValidationMessages;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryRequest {

        @NotBlank(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private String description;
        @NotNull(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private CategoryTypeEnum type;
}
