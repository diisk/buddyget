package br.dev.diisk.presentation.dtos.credit_card;

import java.math.BigDecimal;

import br.dev.diisk.domain.ValidationMessages;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddCreditCardRequest {

        @NotBlank(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private String name;

        @NotNull(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        @Min(value = 1, message = ValidationMessages.NUMBER_MIN1)
        @Max(value = 28, message = ValidationMessages.NUMBER_MAX28)
        private Integer billDueDay;

        @NotNull(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        @Min(value = 1, message = ValidationMessages.NUMBER_MIN1)
        @Max(value = 28, message = ValidationMessages.NUMBER_MAX28)
        private Integer billClosingDay;

        @NotNull(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        @Positive(message = ValidationMessages.POSITIVE_VALUE)
        private BigDecimal cardLimit;

        @NotBlank(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = ValidationMessages.INVALID_COLOR_HEX)
        private String color;
}
