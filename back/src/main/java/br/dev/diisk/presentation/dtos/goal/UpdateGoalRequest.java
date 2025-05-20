package br.dev.diisk.presentation.dtos.goal;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateGoalRequest {
    private String name;
    private String description;
    private String dueDate;
    private BigDecimal targetAmount;
    private String observation;
}
