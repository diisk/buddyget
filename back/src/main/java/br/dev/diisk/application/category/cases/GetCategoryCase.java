package br.dev.diisk.application.category.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.ICategoryRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCategoryCase {

    private final ICategoryRepository categoryRepository;

    @Transactional
    public Category execute(User user, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null || !category.getUserId().equals(user.getId())) {
            throw new DatabaseValueNotFoundException(getClass(), categoryId.toString());
        }
        return category;
    }
}
