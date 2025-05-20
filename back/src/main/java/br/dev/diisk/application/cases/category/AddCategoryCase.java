package br.dev.diisk.application.cases.category;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddCategoryCase {

    private final ICategoryRepository categoryRepository;

    public Category execute(User user, String description, CategoryTypeEnum type) {

        Category category = categoryRepository
                .findBy(user.getId(), type, description).orElse(new Category());

        if (category.getId() != null)
            throw new ValueAlreadyInDatabaseException(getClass(), "description");

        category.setDescription(description);
        category.setType(type);
        category.setUser(user);

        categoryRepository.save(category);

        return category;
    }

}
