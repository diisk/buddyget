package br.dev.diisk.domain.finance.expense_recurring;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.shared.value_objects.DataRange;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

public class ExpenseRecurringFixture {

    public static ExpenseRecurring umaExpenseRecurringComId(Long id) {
        User user = UserFixture.umUsuarioComId(id + 2000);
        Category category = CategoryFixture.umaCategoriaComId(id + 1000, CategoryTypeEnum.EXPENSE, user);
        DataRange period = new DataRange(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now().plusDays(30));

        ExpenseRecurring expenseRecurring = new ExpenseRecurring(
                "Despesa Recorrente Teste",
                period,
                category,
                new BigDecimal("100.00"),
                user);
        expenseRecurring.setId(id);
        return expenseRecurring;
    }

    public static ExpenseRecurring umaExpenseRecurringComId(Long id, User user) {
        Category category = CategoryFixture.umaCategoriaComId(id + 1000, CategoryTypeEnum.EXPENSE, user);
        DataRange period = new DataRange(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now().plusDays(30));

        ExpenseRecurring expenseRecurring = new ExpenseRecurring(
                "Despesa Recorrente Teste",
                period,
                category,
                new BigDecimal("100.00"),
                user);
        expenseRecurring.setId(id);
        return expenseRecurring;
    }
}
