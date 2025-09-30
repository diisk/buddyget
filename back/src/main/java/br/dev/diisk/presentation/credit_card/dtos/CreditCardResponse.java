package br.dev.diisk.presentation.credit_card.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.credit_card.CreditCard;

public record CreditCardResponse(
        Long id,
        String name,
        String billDueDate,
        String billClosingDate,
        BigDecimal cardLimit,
        String color) {
    public CreditCardResponse(CreditCard creditCard, LocalDateTime referenceDate) {
        this(
                creditCard.getId(),
                creditCard.getName(),
                getBillDueDateString(creditCard, referenceDate),
                getBillClosingDateString(creditCard, referenceDate),
                creditCard.getCardLimit(),
                creditCard.getColorString());
    }

    public CreditCardResponse(CreditCard creditCard) {
        this(
                creditCard.getId(),
                creditCard.getName(),
                getBillDueDateString(creditCard, null),
                getBillClosingDateString(creditCard, null),
                creditCard.getCardLimit(),
                creditCard.getColorString());
    }

    private static String getBillDueDateString(CreditCard creditCard, LocalDateTime referenceDate) {
        LocalDateTime dueDate = creditCard.getBillDueDate(referenceDate);
        return dueDate != null ? dueDate.toLocalDate().toString() : null;
    }

    private static String getBillClosingDateString(CreditCard creditCard, LocalDateTime referenceDate) {
        LocalDateTime closingDate = creditCard.getBillClosingDate(referenceDate);
        return closingDate != null ? closingDate.toLocalDate().toString() : null;
    }
}
