package br.dev.diisk.presentation.dtos.creditcard;

import br.dev.diisk.presentation.dtos.transaction.ExpenseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardBillPaymentResponse extends CreditCardBillResponse {
    private ExpenseResponse expense;
}
