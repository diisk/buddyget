package br.dev.diisk.infra.shared.dtos;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GenericResponse {

    private Integer statusCode;
    @Getter(value = lombok.AccessLevel.NONE)
    private Boolean success;
    @Setter(value = AccessLevel.NONE)
    private String date;

    protected GenericResponse(Integer statusCode, Boolean success) {
        this.statusCode = statusCode;
        this.success = success;
        this.date = LocalDateTime.now().toString();
    }

    public Boolean isSuccess() {
        return success;
    }

}
