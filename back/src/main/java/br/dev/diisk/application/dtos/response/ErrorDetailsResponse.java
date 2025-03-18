package br.dev.diisk.application.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDetailsResponse {
    private String message;
    private String objectName;
}
