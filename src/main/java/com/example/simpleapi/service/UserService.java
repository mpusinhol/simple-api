package com.example.simpleapi.service;

import com.example.simpleapi.exception.NotFoundException;
import com.example.simpleapi.exception.OperationNotSupportedException;
import com.example.simpleapi.model.dto.UserDTO;
import com.example.simpleapi.model.dto.UserPatchDTO;
import com.example.simpleapi.model.entity.User;
import com.example.simpleapi.model.mapper.UserMapper;
import com.example.simpleapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    public UserDTO createUser(UserDTO userDTO) {

        User user = userMapper.toEntity(userDTO);
        User newUser = userRepository.save(user);

        return userMapper.toDTO(newUser);
    }

    public void deleteUser(Integer id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        User user = findUserById(id);

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setBirthDate(userDTO.getBirthDate());

        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO patchUser(Integer id, UserPatchDTO userPatchDTO) {
        User user = findUserById(id);

        user.setFirstName(userPatchDTO.getFirstName());
        user.setLastName(userPatchDTO.getLastName());
        user.setGender(userPatchDTO.getGender());

        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO patchUser(Integer id, JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {

        User user = findUserById(id);

        JsonNode patchedNode = jsonPatch.apply(objectMapper.convertValue(user, JsonNode.class));
        User patchedUser = objectMapper.treeToValue(patchedNode, User.class);

        return userMapper.toDTO(userRepository.save(patchedUser));
    }

    public UserDTO findById(Integer id) {
        return userMapper.toDTO(findUserById(id));
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream().map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    private User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", id)));
    }
}
