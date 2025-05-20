package br.dev.diisk.presentation.dtos.creditcard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardBillResponse {
    private Long id;
    private String dueDate;
    private String closingDate;
    private String status;//Em Aberto, Pago, Acumulado
    private CreditCardResponse creditCard;
}
