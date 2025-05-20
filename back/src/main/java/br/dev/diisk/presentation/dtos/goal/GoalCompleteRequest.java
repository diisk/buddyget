package br.dev.diisk.presentation.dtos.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoalCompleteRequest {
    private String description;
    private String paymentDate;
    private Long categoryId;
    private Long creditCardId;
}
