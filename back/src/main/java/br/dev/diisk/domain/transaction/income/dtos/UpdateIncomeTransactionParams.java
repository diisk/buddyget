package br.dev.diisk.domain.transaction.income.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIncomeTransactionParams {
    private String description;
    private BigDecimal value;
    private LocalDateTime receiptDate;
}
