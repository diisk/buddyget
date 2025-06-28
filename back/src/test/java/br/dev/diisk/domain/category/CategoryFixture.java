package br.dev.diisk.domain.category;

import br.dev.diisk.domain.shared.value_objects.HexadecimalColor;
import br.dev.diisk.domain.user.User;

public class CategoryFixture {
    public static Category umaCategoriaComId(Long id, CategoryTypeEnum type, User user) {
        Category categoria = new Category(
                user,
                "Categoria Teste",
                "Descrição Teste",
                "icon-test",
                type,
                new HexadecimalColor("#FFFFFF"));
        categoria.setId(id);
        return categoria;
    }
}
