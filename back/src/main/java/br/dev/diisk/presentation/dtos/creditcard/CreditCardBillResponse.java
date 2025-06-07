package br.dev.diisk.presentation.dtos.creditcard;

public record CreditCardBillResponse(
    Long id,
    String dueDate,
    String closingDate,
    String status,
    CreditCardResponse creditCard
) {}
