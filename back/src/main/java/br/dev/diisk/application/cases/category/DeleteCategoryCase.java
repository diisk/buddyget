package br.dev.diisk.application.cases.category;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteCategoryCase {

    private final ICategoryRepository categoryRepository;

    public void execute(User user, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null && category.getUser().getId() == user.getId())
            categoryRepository.delete(category);

    }

}
