package br.dev.diisk.domain.repositories.category;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.filters.category.ListCategoriesFilter;
import br.dev.diisk.domain.repositories.IBaseRepository;

public interface ICategoryRepository extends IBaseRepository<Category> {

    Optional<Category> findBy(Long userId, CategoryTypeEnum type, String name);

    Page<Category> findAllBy(Long userId, ListCategoriesFilter filter, Pageable pageable);

}
