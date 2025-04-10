package br.dev.diisk.application.cases.category;

import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListCategoriesCase {

    private final ICategoryRepository categoryRepository;

    public List<Category> execute(User user, CategoryTypeEnum type) {

        List<Category> categories = categoryRepository.findAllBy(user.getId(), type);

        return categories;
    }

}
