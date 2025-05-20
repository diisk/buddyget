package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateIncomeRequest {
    private String description;
    private BigDecimal value;
    private String receiptDate;
    private Long categoryId;
}
