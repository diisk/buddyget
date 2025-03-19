package br.dev.diisk.application.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorDetailsResponse {
    private String message;
    private String objectName;
}
