package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateExpenseRequest {
    private String description;
    private BigDecimal value;
    private String paymentDate;
    private String dueDate;
    private Long categoryId;
    private Long creditCardId;
}
