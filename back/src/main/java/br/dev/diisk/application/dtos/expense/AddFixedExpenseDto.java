package br.dev.diisk.application.dtos.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.enums.expense.FixedExpenseTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddFixedExpenseDto {

        private String description;

        private BigDecimal totalAmount;

        private BigDecimal amount;

        private Long categoryId;

        private Long creditCardId;

        private Integer dueDay;

        private LocalDateTime endReference;

        private Integer totalInstallments;

        private LocalDateTime startReference;

        private FixedExpenseTypeEnum type;

}
