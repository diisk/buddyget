package br.dev.diisk.domain.monthly_summary;

import java.math.BigDecimal;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

public class MonthlySummaryFixture {
    
    public static MonthlySummary umMonthlySummaryComId(Long id) {
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.EXPENSE, user);
        
        MonthlySummary monthlySummary = new MonthlySummary(
            user,
            6, // June
            2025,
            new BigDecimal("1500.00"),
            2000L,
            category
        );
        monthlySummary.setId(id);
        return monthlySummary;
    }
    
    public static MonthlySummary umMonthlySummaryComId(Long id, User user, Category category, Integer month, Integer year) {
        MonthlySummary monthlySummary = new MonthlySummary(
            user,
            month,
            year,
            new BigDecimal("1000.00"),
            1500L,
            category
        );
        monthlySummary.setId(id);
        return monthlySummary;
    }
}
