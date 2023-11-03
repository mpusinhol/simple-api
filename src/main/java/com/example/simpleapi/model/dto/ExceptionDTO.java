package com.example.simpleapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class ExceptionDTO {

    private  Integer code;
    private Instant time;
    private String message;
}
