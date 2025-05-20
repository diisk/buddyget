package br.dev.diisk.presentation.dtos.goal;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoalTransactionRequest {
    private String description;
    private BigDecimal value;
    private Long categoryId;
}
