package br.dev.diisk.presentation.dtos.credit_card;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreditCardResponse {
    private Long id;

    private String name;

    private Integer billDueDay;

    private Integer billClosingDay;

    private BigDecimal cardLimit;

    private BigDecimal usedLimit;

    private BigDecimal availableLimit;

    private String color;

    private Boolean active = true;
}
