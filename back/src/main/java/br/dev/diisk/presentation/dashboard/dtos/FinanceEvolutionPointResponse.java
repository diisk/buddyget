package br.dev.diisk.presentation.dashboard.dtos;

import java.math.BigDecimal;

import br.dev.diisk.domain.enums.category.CategoryTypeEnum;

public record FinanceEvolutionPointResponse(
    CategoryTypeEnum type,
    Integer year,
    Integer month,
    BigDecimal value
) {}
