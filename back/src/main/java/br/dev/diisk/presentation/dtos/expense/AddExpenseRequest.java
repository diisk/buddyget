package br.dev.diisk.presentation.dtos.expense;

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
public class AddExpenseRequest {

        @NotBlank(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private String description;

        @Positive(message = ValidationMessages.POSITIVE_VALUE)
        private BigDecimal amount;

        @NotNull(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private Long categoryId;

        private Long creditCardId;

        private LocalDateTime dueDate;

        private LocalDateTime paymentDate;

        private Long fixedExpenseId;

}
