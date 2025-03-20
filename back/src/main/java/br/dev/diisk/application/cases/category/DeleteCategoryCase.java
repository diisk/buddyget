package br.dev.diisk.application.cases.category;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.Category;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteCategoryCase {

    private final ICategoryRepository categoryRepository;

    public void execute(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null && category.getUser().getId() == userId)
            categoryRepository.delete(category);

    }

}
