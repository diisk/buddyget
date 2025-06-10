package br.dev.diisk.presentation.credit_card.dtos;

import br.dev.diisk.presentation.transaction.expense.dtos.ExpenseResponse;

public record CreditCardBillPaymentResponse(
    CreditCardBillResponse bill,
    ExpenseResponse expense
) {}
