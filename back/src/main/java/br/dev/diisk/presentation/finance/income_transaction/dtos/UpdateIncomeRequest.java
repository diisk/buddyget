package br.dev.diisk.presentation.finance.income_transaction.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record UpdateIncomeRequest(
                LocalDateTime paymentDate,
                String description,
                BigDecimal value) {

}
