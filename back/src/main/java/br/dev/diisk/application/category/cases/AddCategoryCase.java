package br.dev.diisk.application.category.cases;

import java.util.Map;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.category.dtos.AddCategoryParams;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.ICategoryRepository;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueConflictException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddCategoryCase {

    private final ICategoryRepository categoryRepository;

    @Transactional
    public Category execute(User user, AddCategoryParams params) {
        String name = params.getName();
        String description = params.getDescription();
        String iconName = params.getIconName();
        String color = params.getColor();
        CategoryTypeEnum type = params.getType();

        if (name == null || name.isBlank())
            throw new NullOrEmptyException(getClass(), "name");

        if (type == null)
            throw new NullOrEmptyException(getClass(), "type");

        Category category = categoryRepository.findBy(user.getId(), type, name).orElse(null);
        if (category != null)
            throw new DatabaseValueConflictException(getClass(), Map.of("name", name, "type", type.name()));

        category = new Category(user, name, description, iconName, type, new HexadecimalColor(color));
        categoryRepository.save(category);
        return category;
    }
}
