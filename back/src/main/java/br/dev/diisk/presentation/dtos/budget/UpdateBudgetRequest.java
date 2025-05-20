package br.dev.diisk.presentation.dtos.budget;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBudgetRequest {
    private String description;
    private String observation;
    private Long limitValue;
}
