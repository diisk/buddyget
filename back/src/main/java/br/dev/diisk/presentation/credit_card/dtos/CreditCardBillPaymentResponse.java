package br.dev.diisk.presentation.credit_card.dtos;

import br.dev.diisk.presentation.finance.expense_transaction.dtos.ExpenseResponse;

public record CreditCardBillPaymentResponse(
    CreditCardBillResponse bill,
    ExpenseResponse expense
) {}
