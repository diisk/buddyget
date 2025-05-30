package br.dev.diisk.application.cases.category;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.dtos.category.UpdateCategoryDto;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.exceptions.persistence.ValueAlreadyInDatabaseException;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateCategoryCase {

    private final ICategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final GetCategoryCase getCategoryCase;

    public Category execute(User user, Long categoryId, UpdateCategoryDto dto) {
        Category category = getCategoryCase.execute(user, categoryId);

        Category categoryExisting = categoryRepository
                .findBy(user.getId(), category.getType(), dto.getDescription()).orElse(null);

        if (categoryExisting != null && categoryExisting.getId() != category.getId())
            throw new ValueAlreadyInDatabaseException(getClass(), "description");

        modelMapper.map(dto, category);

        categoryRepository.save(category);

        return category;
    }

}
