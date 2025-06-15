package br.dev.diisk.application.monthly_summary.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMonthlySummaryParams {
    private Integer month;
    private Integer year;
    private Long categoryId;
}
