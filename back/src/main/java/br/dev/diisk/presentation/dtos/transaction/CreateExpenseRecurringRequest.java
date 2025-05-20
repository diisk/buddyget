package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateExpenseRecurringRequest {
    private String description;
    private String startDate;
    private BigDecimal value;
    private BigDecimal totalValue;
    private Integer installmentsCount;
    private String endDate;
    private String dueDay;
    private String recurringDay;
    private Long categoryId;
    private Long creditCardId;
}
