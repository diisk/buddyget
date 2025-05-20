package br.dev.diisk.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomRuntimeException extends RuntimeException{
    private Class<?> classObject;
    private String message;
}
