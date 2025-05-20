package br.dev.diisk.presentation.dtos.creditcard;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PayCreditCardBillRequest {
    private String description;
    private String paymentDate;
    private BigDecimal value;
    private Long categoryId;
}
