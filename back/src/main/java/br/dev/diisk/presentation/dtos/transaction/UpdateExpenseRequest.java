package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateExpenseRequest {
    private String description;
    private BigDecimal value;
    private String paymentDate;
    private String dueDate;
}
