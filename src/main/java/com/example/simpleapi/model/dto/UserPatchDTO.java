package com.example.simpleapi.model.dto;

import com.example.simpleapi.model.entity.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class UserPatchDTO implements Serializable {

    private String firstName;
    private String lastName;
    private Gender gender;
}
