package br.dev.diisk.presentation.dtos.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.ValidationMessages;
import br.dev.diisk.domain.enums.expense.FixedExpenseTypeEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateFixedExpenseRequest {

        private String description;
        
        @Positive(message = ValidationMessages.POSITIVE_VALUE)
        private BigDecimal totalAmount;

        @Positive(message = ValidationMessages.POSITIVE_VALUE)
        private BigDecimal amount;

        private Long categoryId;

        private Long creditCardId;

        @Min(value = 1, message = ValidationMessages.NUMBER_MIN1)
        @Max(value = 28, message = ValidationMessages.NUMBER_MAX28)
        private Integer dueDay;

        private LocalDateTime endReference;

        private Integer totalInstallments;

        private LocalDateTime startReference;

        private FixedExpenseTypeEnum type;

}
