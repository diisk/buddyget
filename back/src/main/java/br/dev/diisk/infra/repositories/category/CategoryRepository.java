package br.dev.diisk.infra.repositories.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.repositories.category.ICategoryRepository;
import br.dev.diisk.infra.jpas.category.CategoryJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class CategoryRepository extends BaseRepository<CategoryJPA, Category> implements ICategoryRepository {

    public CategoryRepository(CategoryJPA jpa) {
        super(jpa);
    }

    @Override
    public Optional<Category> findBy(Long userId, CategoryTypeEnum type, String description) {
        return jpa.findByUser_IdAndTypeAndDescriptionAndDeletedFalse(userId, type, description);
    }

    @Override
    public List<Category> findAllBy(Long userId, CategoryTypeEnum type) {
        return jpa.findByUser_IdAndTypeAndDeletedFalse(userId, type);
    }

}
