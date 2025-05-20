package br.dev.diisk.presentation.dtos.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateIncomeRecurringRequest {
    private String description;
    private String recurringDay;
}
