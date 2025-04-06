package br.dev.diisk.presentation.dtos.expense;

import java.math.BigDecimal;

import br.dev.diisk.domain.enums.expense.FixedExpenseTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FixedExpenseResponse {

        private Long id;

        private String description;

        private String categoryName;

        private Long categoryId;

        private Long creditCardId;

        private Integer dueDay;

        private String endReference;

        private BigDecimal totalAmount;
        
        private BigDecimal amount;

        private Integer totalInstallments;

        private String startReference;

        private FixedExpenseTypeEnum type;

        private String typeName;

}
