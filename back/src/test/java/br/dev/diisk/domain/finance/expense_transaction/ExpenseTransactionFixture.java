package br.dev.diisk.domain.finance.expense_transaction;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.user.User;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenseTransactionFixture {
    public static ExpenseTransaction umaTransacaoComId(Long id, User user, Category category) {
        ExpenseTransaction tx = new ExpenseTransaction(
                "Descrição original",
                category,
                new BigDecimal("100.00"),
                LocalDateTime.of(2024, 6, 1, 12, 0),
                user);
        tx.setId(id);
        return tx;
    }

    public static ExpenseTransaction umaTransacaoComExpenseRecurring(Long id, User user, Category category, 
                                                                     ExpenseRecurring expenseRecurring, 
                                                                     LocalDateTime recurringReferenceDate) {
        ExpenseTransaction tx = new ExpenseTransaction(
                "Descrição original",
                category,
                new BigDecimal("100.00"),
                null, // sem paymentDate para ser pendente
                user);
        tx.setId(id);
        
        try {
            // Configura ExpenseRecurring usando reflexão
            Field expenseRecurringField = ExpenseTransaction.class.getDeclaredField("expenseRecurring");
            expenseRecurringField.setAccessible(true);
            expenseRecurringField.set(tx, expenseRecurring);
            
            // Configura recurringReferenceDate usando reflexão
            Field recurringReferenceDateField = tx.getClass().getSuperclass().getDeclaredField("recurringReferenceDate");
            recurringReferenceDateField.setAccessible(true);
            recurringReferenceDateField.set(tx, recurringReferenceDate);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar ExpenseRecurring na fixture", e);
        }
        
        return tx;
    }
}
