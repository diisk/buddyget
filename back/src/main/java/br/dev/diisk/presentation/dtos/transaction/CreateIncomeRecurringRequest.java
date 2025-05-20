package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateIncomeRecurringRequest {
    private String description;
    private String startDate;
    private BigDecimal value;
    private String endDate;
    private String recurringDay;
    private Long categoryId;
}
