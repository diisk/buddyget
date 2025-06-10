package br.dev.diisk.presentation.credit_card.dtos;

public record CreditCardBillResponse(
    Long id,
    String dueDate,
    String closingDate,
    String status,
    CreditCardResponse creditCard
) {}
