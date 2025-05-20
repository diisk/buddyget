package br.dev.diisk.presentation.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentResponse {
    private Long id;
    private Double amount;
    private String dueDate;
    private Boolean paid;
}
