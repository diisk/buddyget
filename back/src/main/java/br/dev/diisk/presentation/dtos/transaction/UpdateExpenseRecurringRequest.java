package br.dev.diisk.presentation.dtos.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateExpenseRecurringRequest {
    private String description;
    private String dueDay;
    private String recurringDay;
}
