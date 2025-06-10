package br.dev.diisk.domain.category;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface ICategoryRepository extends IBaseRepository<Category> {

    Optional<Category> findBy(Long userId, CategoryTypeEnum type, String name);

    Page<Category> findAllBy(Long userId, ListCategoriesFilter filter, Pageable pageable);

}
