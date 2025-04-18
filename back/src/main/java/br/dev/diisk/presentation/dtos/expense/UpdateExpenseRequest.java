package br.dev.diisk.presentation.dtos.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.ValidationMessages;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateExpenseRequest {

        private String description;

        @Positive(message = ValidationMessages.POSITIVE_VALUE)
        private BigDecimal amount;

        private Long categoryId;

        private Long creditCardId;

        private LocalDateTime dueDate;

        private LocalDateTime paymentDate;

}
