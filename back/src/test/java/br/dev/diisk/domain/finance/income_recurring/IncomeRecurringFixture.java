package br.dev.diisk.domain.finance.income_recurring;

import java.math.BigDecimal;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.shared.value_objects.Period;
import br.dev.diisk.domain.shared.value_objects.DayOfMonth;
import br.dev.diisk.domain.user.User;

public class IncomeRecurringFixture {
    public static IncomeRecurring umIncomeRecurringComId(Long id, User user, Category category, DayOfMonth recurringDay, Period period) {
        IncomeRecurring incomeRecurring = new IncomeRecurring(
                "Receita Recorrente Teste",
                period,
                category,
                new BigDecimal("100.00"),
                user
        );
        incomeRecurring.setId(id);
        return incomeRecurring;
    }
}
