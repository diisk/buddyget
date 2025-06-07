package br.dev.diisk.presentation.dtos.creditcard;

import br.dev.diisk.presentation.dtos.transaction.ExpenseResponse;

public record CreditCardBillPaymentResponse(
    CreditCardBillResponse bill,
    ExpenseResponse expense
) {}
