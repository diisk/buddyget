package br.dev.diisk.application.dtos.income;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateIncomeDto {

        private String description;

        private BigDecimal amount;

        private LocalDateTime receiptDate;

        private Long categoryId;
}
