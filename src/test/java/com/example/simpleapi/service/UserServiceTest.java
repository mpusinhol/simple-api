package com.example.simpleapi.service;

import com.example.simpleapi.exception.NotFoundException;
import com.example.simpleapi.model.dto.UserDTO;
import com.example.simpleapi.model.entity.Gender;
import com.example.simpleapi.model.entity.User;
import com.example.simpleapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.example.simpleapi.fixtures.TestFixtures.USER;
import static com.example.simpleapi.fixtures.TestFixtures.USER_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    private final UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        when(userRepository.findAll()).thenReturn(List.of(USER));
        when(userRepository.findById(1)).thenReturn(Optional.of(USER));
        when(userRepository.save(any())).thenReturn(USER);
        doNothing().when(userRepository).delete(any());
    }


    @Nested
    @DisplayName("Find All Tests")
    class FindAllTests {
        @Test
        void testFindAll_happyFlow() {
            List<UserDTO> users = userService.findAll();
            assertNotNull(users);
            assertEquals(1, users.size());

            UserDTO user = users.get(0);
            assertEquals(USER_DTO, user);
        }
    }

    @Nested
    @DisplayName("Find By Id Tests")
    class FindByIdTests {

        @Test
        void testFindById_happyFlow() {
            UserDTO user = userService.findById(1);

            assertNotNull(user);
            assertEquals(USER_DTO, user);
        }

        @Test
        void testFindById_userNotFound() {
            assertThrows(NotFoundException.class, () -> userService.findById(2), "User with id 2 not found");
        }
    }

    @Nested
    @DisplayName("Create User Tests")
    class CreateUserTests {

        @Test
        void testCreateUser_happyFlow() {
            UserDTO user = userService.createUser(USER_DTO);

            assertNotNull(user);
            assertEquals(USER_DTO, user);
        }
    }

    @Nested
    @DisplayName("Delete User Tests")
    class DeleteUserTests {

        @Test
        void testDeleteUser_happyFlow() {
            userService.deleteUser(1);
            verify(userRepository, times(1)).findById(1);
            verify(userRepository, times(1)).delete(USER);
        }

        @Test
        void testDeleteUser_userNotFound() {
            assertThrows(NotFoundException.class, () -> userService.deleteUser(2), "User with id 2 not found");
        }
    }

    @Nested
    @DisplayName("Update User Tests")
    class UpdateUserTests {

        @Test
        void testUpdateUser_happyFlow() {
            User user = new User(USER.getId(), USER.getFirstName(), "Travolta", USER.getAge(), USER.getGender(), USER.getBirthDate());
            when(userRepository.findById(1)).thenReturn(Optional.of(user));

            UserDTO userDTO = userService.updateUser(1, USER_DTO);

            assertNotNull(userDTO);
            assertEquals(USER_DTO.getLastName(), userDTO.getLastName());
        }

        @Test
        void testUpdateUser_userNotFound() {
            assertThrows(NotFoundException.class, () -> userService.updateUser(2, USER_DTO), "User with id 2 not found");
        }
    }
}
