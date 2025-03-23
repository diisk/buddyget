package br.dev.diisk.presentation.dtos.credit_card;

import java.math.BigDecimal;

import br.dev.diisk.domain.ValidationMessages;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCreditCardRequest {

        @Size(max = 100)
        private String name;

        @Min(value = 1, message = ValidationMessages.NUMBER_MIN1)
        @Max(value = 28, message = ValidationMessages.NUMBER_MAX28)
        private Integer billDueDay;

        @Min(value = 1, message = ValidationMessages.NUMBER_MIN1)
        @Max(value = 28, message = ValidationMessages.NUMBER_MAX28)
        private Integer billClosingDay;

        @Positive(message = ValidationMessages.POSITIVE_VALUE)
        private BigDecimal cardLimit;
        
        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = ValidationMessages.INVALID_COLOR_HEX)
        private String color;

        private Boolean active;
}
