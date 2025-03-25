package br.dev.diisk.application.dtos.response;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private final List<T> content;
    private final int totalPages;

}
