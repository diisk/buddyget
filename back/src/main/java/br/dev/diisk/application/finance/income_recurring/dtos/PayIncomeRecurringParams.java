package br.dev.diisk.application.finance.income_recurring.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayIncomeRecurringParams {
    private LocalDateTime paymentDate;
    private LocalDateTime referenceDate;
}
