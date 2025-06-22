package br.dev.diisk.application.monthly_summary.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMonthlySummaryParams {
    private LocalDateTime previousDate;
    private LocalDateTime newDate;
    private BigDecimal previousValue;
    private BigDecimal newValue;
    private Category category;
}
