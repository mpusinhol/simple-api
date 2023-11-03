package com.example.simpleapi.model.dto;

import com.example.simpleapi.model.entity.Gender;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Getter
@EqualsAndHashCode
public class UserDTO implements Serializable {

    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Gender gender;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("age") Integer age,
            @JsonProperty("gender") Gender gender,
            @JsonProperty("birth_date") LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.birthDate = birthDate;
    }
}
