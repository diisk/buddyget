package br.dev.diisk.presentation.dtos.creditcard;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCreditCardRequest {
    private String name;
    private Integer billDueDay;
    private Integer billClosingDay;
    private BigDecimal cardLimit;
    private String color;
}
