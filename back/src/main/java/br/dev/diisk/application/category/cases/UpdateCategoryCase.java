package br.dev.diisk.application.category.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.category.dtos.UpdateCategoryParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.ICategoryRepository;
import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateCategoryCase {

    private final GetCategoryCase getCategoryCase;
    private final ICategoryRepository categoryRepository;

    @Transactional
    public Category execute(User user, Long categoryId, UpdateCategoryParams params) {
        Category category = getCategoryCase.execute(user, categoryId);
        Boolean save = category.update(
                params.getName(),
                params.getDescription(),
                params.getIconName(),
                params.getColor() != null ? new HexadecimalColor(params.getColor()) : null);

        if (save)
            categoryRepository.save(category);
        return category;
    }
}
