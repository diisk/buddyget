package br.dev.diisk.application.cases.category;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
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
