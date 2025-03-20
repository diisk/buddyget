package br.dev.diisk.application.cases.category;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.entities.Category;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCategoryCase {

    private final ICategoryRepository categoryRepository;

    public Category execute(Long userId, Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null || category.getUser().getId() != userId) 
            throw new DbValueNotFoundException(getClass(), "id");

        return category;
    }

}
