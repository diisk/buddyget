package br.dev.diisk.presentation.dtos.income;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncomeResponse {

        private Long id;

        private String description;

        private BigDecimal amount;

        private String receiptDate;

        private Long categoryId;

        private String categoryName;

        private String createdAt;
}
