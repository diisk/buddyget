package br.dev.diisk.presentation.dtos.transaction;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateIncomeRequest {
    private String receiptDate;
    private String description;
    private BigDecimal value;
}
