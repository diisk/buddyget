package br.dev.diisk.application.budget.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.budget.Budget;
import br.dev.diisk.domain.budget.IBudgetRepository;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetBudgetCase {

    private final IBudgetRepository budgetRepository;

    @Transactional
    public Budget execute(User user, Category category) {
        return budgetRepository.findByCategory(user.getId(), category.getId()).orElse(null);
    }
}
