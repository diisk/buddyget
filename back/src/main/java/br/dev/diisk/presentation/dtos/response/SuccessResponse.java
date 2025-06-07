package br.dev.diisk.presentation.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> extends GenericResponse{

    private T content;

    public SuccessResponse(T content, Integer statusCode) {
        super(statusCode,true);
        this.content = content;
    }

}
