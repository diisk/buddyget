package br.dev.diisk.presentation.dtos.income;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddIncomeRequest {

        @NotBlank(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private String description;

        @NotNull(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        @Positive(message = ValidationMessages.POSITIVE_VALUE)
        private BigDecimal amount;

        @NotNull(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private LocalDateTime receiptDate;

        @NotNull(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private Long categoryId;
}
