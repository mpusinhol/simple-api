package com.example.simpleapi.model.mapper;

import com.example.simpleapi.model.dto.UserDTO;
import com.example.simpleapi.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .age(userDTO.getAge())
                .gender(userDTO.getGender())
                .birthDate(userDTO.getBirthDate())
                .build();
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .build();
    }

}
