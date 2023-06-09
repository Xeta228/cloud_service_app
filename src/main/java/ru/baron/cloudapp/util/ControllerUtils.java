package ru.baron.cloudapp.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


import java.util.Map;
import java.util.stream.Collectors;

public class ControllerUtils {
    public static Map<String, String> getErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(Collectors
                .toMap(fe -> fe.getField() + "Error", FieldError::getDefaultMessage));
    }
}
