package br.dev.diisk.domain.interfaces;

import java.util.Optional;

public interface IBaseEnum {

    String getDescription();

    static <E extends Enum<E> & IBaseEnum> Optional<E> getByString(Class<E> enumClass, String value) {
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return Optional.of(enumConstant);
            }
        }
        return Optional.empty();
    }

}
