package br.dev.diisk.presentation.dtos.income;

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
public class UpdateIncomeRequest {

        private String description;
        @Positive(message = ValidationMessages.POSITIVE_VALUE)
        private BigDecimal amount;
        private LocalDateTime receiptDate;
        private Long categoryId;
}
