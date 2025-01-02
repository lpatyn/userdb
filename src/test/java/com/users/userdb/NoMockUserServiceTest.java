package com.users.userdb;

import com.users.userdb.dto.request.NewPasswordDTO;
import com.users.userdb.dto.request.NewUserDTO;
import com.users.userdb.dto.request.UpdateUserDTO;
import com.users.userdb.dto.response.UserDTO;
import com.users.userdb.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.users.userdb.constants.ApplicationConstants.DATE_FORMAT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NoMockUserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void createTest() throws Exception {

        try {
            userService.delete(1);
        } catch (Exception ignored) {}

        NewUserDTO newUser = new NewUserDTO("Jorge", "Pruebas", "jorge@email.com",
                DATE_FORMAT.parse("01/01/1990"), "prueba");

        UserDTO expected = new UserDTO(1, "Jorge", "Pruebas", "jorge@email.com",
                "01/01/1990");

        UserDTO created = userService.create(newUser);

        UserDTO found = userService.findById(1);

        assertAll(() -> {
            assertEquals(expected.getId(), created.getId());
            assertEquals(expected.getName(), created.getName());
            assertEquals(expected.getLastName(), created.getLastName());
            assertEquals(expected.getEmail(), created.getEmail());
            assertEquals(expected.getBirthdate(), created.getBirthdate());
            assertEquals(expected.getId(), found.getId());
            assertEquals(expected.getName(), found.getName());
            assertEquals(expected.getLastName(), found.getLastName());
            assertEquals(expected.getEmail(), found.getEmail());
            assertEquals(expected.getBirthdate(), found.getBirthdate());
        });
    }

    @Test
    void updateTest() throws Exception {

        try {
            userService.delete(1);
        } catch (Exception ignored) {}

        NewUserDTO newUser = new NewUserDTO("Jorge", "Pruebas", "jorge@email.com",
                DATE_FORMAT.parse("01/01/1990"), "prueba");

        userService.create(newUser);

        UserDTO expected = new UserDTO(1, "Jorgito", "Test", "test@email.com",
                "01/01/1999");

        UpdateUserDTO toUpdate = new UpdateUserDTO("Jorgito", "Test", "test@email.com",
                DATE_FORMAT.parse("01/01/1999"));

        UserDTO updated = userService.update(1, toUpdate);

        UserDTO found = userService.findById(1);

        assertAll(() -> {
            assertEquals(expected.getId(), updated.getId());
            assertEquals(expected.getName(), updated.getName());
            assertEquals(expected.getLastName(), updated.getLastName());
            assertEquals(expected.getEmail(), updated.getEmail());
            assertEquals(expected.getBirthdate(), updated.getBirthdate());
            assertEquals(expected.getId(), found.getId());
            assertEquals(expected.getName(), found.getName());
            assertEquals(expected.getLastName(), found.getLastName());
            assertEquals(expected.getEmail(), found.getEmail());
            assertEquals(expected.getBirthdate(), found.getBirthdate());
        });
    }

    @Test
    void changePasswordTest() throws Exception {

        try {
            userService.delete(1);
        } catch (Exception ignored) {}

        NewUserDTO newUser = new NewUserDTO("Jorge", "Pruebas", "jorge@email.com",
                DATE_FORMAT.parse("01/01/1990"), "prueba");

        userService.create(newUser);

        UserDTO expected = new UserDTO(1, "Jorge", "Pruebas", "jorge@email.com",
                "01/01/1990");

        NewPasswordDTO toUpdate = new NewPasswordDTO("jorge@email.com",
                "pruebas2");

        UserDTO changed = userService.changePassword(toUpdate);

        UserDTO found = userService.findById(1);

        assertAll(() -> {
            assertEquals(expected.getId(), changed.getId());
            assertEquals(expected.getName(), changed.getName());
            assertEquals(expected.getLastName(), changed.getLastName());
            assertEquals(expected.getEmail(), changed.getEmail());
            assertEquals(expected.getBirthdate(), changed.getBirthdate());
            assertEquals(expected.getId(), found.getId());
            assertEquals(expected.getName(), found.getName());
            assertEquals(expected.getLastName(), found.getLastName());
            assertEquals(expected.getEmail(), found.getEmail());
            assertEquals(expected.getBirthdate(), found.getBirthdate());
        });
    }
}
