package br.dev.diisk.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomRuntimeException extends RuntimeException{
    private Class<?> classObject;
    private String message;
}
