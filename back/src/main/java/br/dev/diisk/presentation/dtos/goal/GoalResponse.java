package br.dev.diisk.presentation.dtos.goal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoalResponse {
    private Long id;
    private String name;
    private String description;
    private String dueDate;
    private Double targetAmount;
    private Double currentAmount;
    private String status;
    private Long categoryId;
}
