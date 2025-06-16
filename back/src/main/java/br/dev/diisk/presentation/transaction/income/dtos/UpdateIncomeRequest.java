package br.dev.diisk.presentation.transaction.income.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record UpdateIncomeRequest(
                LocalDateTime receiptDate,
                String description,
                BigDecimal value) {

}
