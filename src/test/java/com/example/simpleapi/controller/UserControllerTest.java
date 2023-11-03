package com.example.simpleapi.controller;

import com.example.simpleapi.exception.NotFoundException;
import com.example.simpleapi.model.dto.ExceptionDTO;
import com.example.simpleapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.example.simpleapi.fixtures.TestFixtures.USER_DTO;
import static com.example.simpleapi.fixtures.TestFixtures.USER_DTO_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        when(userService.findAll()).thenReturn(List.of(USER_DTO));
        when(userService.findById(1)).thenReturn(USER_DTO);
        when(userService.findById(2)).thenThrow(new NotFoundException("User with id 2 not found"));
        when(userService.createUser(any())).thenReturn(USER_DTO);
        when(userService.updateUser(eq(1), any())).thenReturn(USER_DTO);
        when(userService.updateUser(eq(2), any())).thenThrow(new NotFoundException("User with id 2 not found"));
        doNothing().when(userService).deleteUser(1);
        doThrow(new NotFoundException("User with id 2 not found")).when(userService).deleteUser(2);
    }

    @Nested
    @DisplayName("Get /Users Test")
    class GetUsersTest {


        @Test
        void getUsers_happyFlow() throws Exception {
            String response = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn().getResponse().getContentAsString();

            assertNotNull(response);
            JSONAssert.assertEquals(String.format("[%s]", USER_DTO_JSON), response, JSONCompareMode.LENIENT);
        }
    }

    @Nested
    @DisplayName("Get /Users/1 Test")
    class GetUserTest {


        @Test
        void getUser_happyFlow() throws Exception {
            String response = mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn().getResponse().getContentAsString();

            assertNotNull(response);
            JSONAssert.assertEquals(USER_DTO_JSON, response, JSONCompareMode.LENIENT);
        }

        @Test
        void getUser_notFound() throws Exception {
            String response = mockMvc.perform(MockMvcRequestBuilders.get("/users/2"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn().getResponse().getContentAsString();

            assertNotNull(response);

            ExceptionDTO exceptionDTO = objectMapper.readValue(response, ExceptionDTO.class);
            assertEquals(404, exceptionDTO.getCode());
            assertEquals("User with id 2 not found", exceptionDTO.getMessage());
        }
    }

    @Nested
    @DisplayName("Post /Users Test")
    class PostUserTest {


        @Test
        void postUser_happyFlow() throws Exception {
            String url = mockMvc.perform(
                            MockMvcRequestBuilders.post("/users")
                                    .content(USER_DTO_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn().getResponse().getRedirectedUrl();

            assertNotNull(url);
            assertTrue(url.endsWith("/users/1"));
        }
    }

    @Nested
    @DisplayName("PUT /Users Test")
    class PutUserTest {


        @Test
        void putUser_happyFlow() throws Exception {
            String response = mockMvc.perform(
                            MockMvcRequestBuilders.put("/users/1")
                                    .content(USER_DTO_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn().getResponse().getContentAsString();

            assertNotNull(response);
            JSONAssert.assertEquals(USER_DTO_JSON, response, JSONCompareMode.LENIENT);
        }

        @Test
        void putUser_notFound() throws Exception {
            String response = mockMvc.perform(
                            MockMvcRequestBuilders.put("/users/2")
                                    .content(USER_DTO_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn().getResponse().getContentAsString();

            assertNotNull(response);
            ExceptionDTO exceptionDTO = objectMapper.readValue(response, ExceptionDTO.class);
            assertEquals(404, exceptionDTO.getCode());
            assertEquals("User with id 2 not found", exceptionDTO.getMessage());
        }
    }

    @Nested
    @DisplayName("DELETE /Users/1 Test")
    class DeleteUserTest {


        @Test
        void deleteUser_happyFlow() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.delete("/users/1")
                    )
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

            verify(userService, times(1)).deleteUser(1);
        }

        @Test
        void deleteUser_notFound() throws Exception {
            String response = mockMvc.perform(
                            MockMvcRequestBuilders.delete("/users/2")
                    )
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn().getResponse().getContentAsString();

            assertNotNull(response);
            ExceptionDTO exceptionDTO = objectMapper.readValue(response, ExceptionDTO.class);
            assertEquals(404, exceptionDTO.getCode());
            assertEquals("User with id 2 not found", exceptionDTO.getMessage());
        }
    }
}
