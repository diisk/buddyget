package br.dev.diisk.domain.repositories.category;

import java.util.List;
import java.util.Optional;

import br.dev.diisk.domain.entities.Category;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.IBaseRepository;

public interface ICategoryRepository extends IBaseRepository<Category> {

    Optional<Category> findBy(Long userId, CategoryTypeEnum type, String description);

    List<Category> findByUserAndType(Long userId, CategoryTypeEnum type);

}
