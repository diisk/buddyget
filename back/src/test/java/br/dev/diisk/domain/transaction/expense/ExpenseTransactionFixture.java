package br.dev.diisk.domain.transaction.expense;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenseTransactionFixture {
    public static ExpenseTransaction umaTransacaoComId(Long id, User user, Category category) {
        ExpenseTransaction tx = new ExpenseTransaction(
                "Descrição original",
                category,
                new BigDecimal("100.00"),
                LocalDateTime.of(2024, 6, 1, 12, 0),
                user
        );
        tx.setId(id);
        return tx;
    }
}
