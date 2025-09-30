package br.dev.diisk.application.credit_card.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCreditCardParams {
    private String name;
    private Integer billDueDay;
    private Integer billClosingDay;
    private BigDecimal cardLimit;
    private String color;
}
