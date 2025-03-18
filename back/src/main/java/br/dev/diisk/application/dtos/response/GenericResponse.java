package br.dev.diisk.application.dtos.response;


import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse {

    private Integer statusCode;
    private Boolean success;
    @Setter(value = AccessLevel.NONE)
    private String date;

    protected GenericResponse(Integer statusCode, Boolean success) {
        this.statusCode = statusCode;
        this.success = success;
        this.date = LocalDateTime.now().toString();
    }

}
