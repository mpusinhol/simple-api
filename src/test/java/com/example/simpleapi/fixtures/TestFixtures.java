package com.example.simpleapi.fixtures;

import com.example.simpleapi.model.dto.UserDTO;
import com.example.simpleapi.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFixtures {

    private static final ObjectMapper OBJECT_MAPPER = configureObjectMapper();

    public static final User USER = getResource("/fixtures/user.json", User.class);
    public static final String USER_JSON = getResource("/fixtures/user.json");
    public static final UserDTO USER_DTO = getResource("/fixtures/user_dto.json", UserDTO.class);
    public static final String USER_DTO_JSON = getResource("/fixtures/user_dto.json");

    private static <T> T getResource(String path, Class<T> classType) {
        try (InputStream resource = TestFixtures.class.getResourceAsStream(path)) {
            return OBJECT_MAPPER.readValue(resource, classType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static String getResource(String path) {
        URL resource = TestFixtures.class.getResource(path);
        return Files.readString(Paths.get(resource.toURI()));
    }

    private static ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
