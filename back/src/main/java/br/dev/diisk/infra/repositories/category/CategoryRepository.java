package br.dev.diisk.infra.repositories.category;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.filters.category.ListCategoriesFilter;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import br.dev.diisk.infra.jpas.category.CategoryJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class CategoryRepository extends BaseRepository<CategoryJPA, Category> implements ICategoryRepository {

    public CategoryRepository(CategoryJPA jpa) {
        super(jpa);
    }

    @Override
    public Optional<Category> findBy(Long userId, CategoryTypeEnum type, String name) {
        return jpa.findByUser_IdAndTypeAndNameAndDeletedFalse(userId, type, name);
    }

    @Override
    public Page<Category> findAllBy(Long userId, ListCategoriesFilter filter, Pageable pageable) {
        return jpa.findAllByUser(userId, filter.getType(), filter.getSearchString(), pageable);
    }

}
