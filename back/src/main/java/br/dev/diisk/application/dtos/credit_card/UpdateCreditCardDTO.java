package br.dev.diisk.application.dtos.credit_card;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCreditCardDto {

        private String name;

        private Integer billDueDay;

        private Integer billClosingDay;

        private BigDecimal cardLimit;

        private String color;

        private Boolean active;
}
