package br.dev.diisk.application.category.cases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.ICategoryRepository;
import br.dev.diisk.domain.category.ListCategoriesFilter;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListCategoriesCase {

    private final ICategoryRepository categoryRepository;

    @Transactional
    public Page<Category> execute(User user, ListCategoriesFilter filter, Pageable pageable) {
        Page<Category> categories = categoryRepository.findAllBy(user.getId(), filter, pageable);
        return categories;
    }
}
