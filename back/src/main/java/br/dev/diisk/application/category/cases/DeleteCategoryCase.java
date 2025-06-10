package br.dev.diisk.application.category.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.ICategoryRepository;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteCategoryCase {

    private final ICategoryRepository categoryRepository;

    @Transactional
    public void execute(User user, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            category.delete();
            categoryRepository.save(category);
        }
    }
}
