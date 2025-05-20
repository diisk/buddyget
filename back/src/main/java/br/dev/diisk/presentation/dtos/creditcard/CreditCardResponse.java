package br.dev.diisk.presentation.dtos.creditcard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardResponse {
    private Long id;
    private String name;
    private String billDueDate;
    private String billClosingDate;
    private BigDecimal cardLimit;
    private BigDecimal usedLimit;
    private BigDecimal availableLimit;
    private String color;
}
