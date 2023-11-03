package com.example.simpleapi.controller;

import com.example.simpleapi.model.dto.UserDTO;
import com.example.simpleapi.model.dto.UserPatchDTO;
import com.example.simpleapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.createUser(userDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> patchUser(@PathVariable Integer id, @RequestBody UserPatchDTO userPatchDTO) {
        UserDTO patchedUser = userService.patchUser(id, userPatchDTO);

        return ResponseEntity.ok(patchedUser);
    }

    @PatchMapping("/{id}/patch")
    public ResponseEntity<UserDTO> patchUser(@PathVariable("id") Integer id, @RequestBody JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        UserDTO patchedUser = userService.patchUser(id, jsonPatch);

        return ResponseEntity.ok(patchedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        UserDTO userDTO = userService.findById(id);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findById() {
        List<UserDTO> users = userService.findAll();

        return ResponseEntity.ok(users);
    }
}
