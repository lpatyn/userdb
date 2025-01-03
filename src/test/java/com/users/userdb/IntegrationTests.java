package com.users.userdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.users.userdb.dto.request.NewPasswordDTO;
import com.users.userdb.dto.request.NewUserDTO;
import com.users.userdb.dto.request.UpdateUserDTO;
import com.users.userdb.dto.response.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static com.users.userdb.constants.ApplicationConstants.DATE_FORMAT;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void resourceNotFoundTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Requested resource not found"),
                        jsonPath("$.errors.key").value("path"),
                        jsonPath("$.errors.value").value("/api/user")
                );
    }

    @Test
    void userNotFoundTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/999"))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Requested user not found"),
                        jsonPath("$.errors.key").value("id"),
                        jsonPath("$.errors.value").value("999")
                );
    }

    @Test
    void illegalArgumentTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1a"))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Value of id must be numeric"),
                        jsonPath("$.errors.key").value("id"),
                        jsonPath("$.errors.value").value("1a")
                );
    }

    @Test
    void invalidFieldTest() throws Exception {
        NewUserDTO payload = new NewUserDTO("A", "A", "not_an_email",
                null, "test");

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE , false)
                .writer().withDefaultPrettyPrinter();

        String payloadJson = writer.writeValueAsString(payload);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Invalid field data"),
                        jsonPath("$.errors.[?(@.field == 'name' && " +
                                "@.reason == 'Field must be a minimum character length of 3' &&" +
                                "@.value == 'A')]")
                                .exists(),
                        jsonPath("$.errors.[?(@.field == 'lastName' && " +
                                "@.reason == 'Field must be a minimum character length of 3' &&" +
                                "@.value == 'A')]")
                                .exists(),
                        jsonPath("$.errors.[?(@.field == 'email' && " +
                                "@.reason == 'Field must be a valid email format' &&" +
                                "@.value == 'not_an_email')]")
                                .exists(),
                        jsonPath("$.errors.[?(@.field == 'password' && " +
                                "@.reason == 'Field must be a minimum character length of 6' &&" +
                                "@.value == 'test')]")
                                .exists(),
                        jsonPath("$.errors.[?(@.field == 'birthdate' && " +
                                "@.reason == 'Field cannot be null' &&" +
                                "@.value == null)]")
                                .exists()
                );
    }

    @Test
    void futureDateErrorTest() throws Exception {
        NewUserDTO payload = new NewUserDTO("Jorge", "Test", "email@email.com",
                DATE_FORMAT.parse("31/12/2099"), "testing");

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE , false)
                .writer().withDefaultPrettyPrinter();

        String payloadJson = writer.writeValueAsString(payload);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Invalid field data"),
                        jsonPath("$.errors.[?(@.field == 'birthdate' && " +
                                "@.reason == 'Field must be a date in the past' &&" +
                                "@.value == '31/12/2099')]")
                                .exists()
                );
    }

    @Test
    void emptyFindAllTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("Success"),
                        jsonPath("$.data").value(new ArrayList<>())
                );
    }

    @Test
    void crudTest() throws Exception {

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE , false)
                .writer();

        UserDTO comparator = new UserDTO(1, "Jorge", "Pruebas", "jorge@email.com",
                "01/01/1990");

        NewUserDTO newUser = new NewUserDTO("Jorge", "Pruebas", "jorge@email.com",
                DATE_FORMAT.parse("01/01/1990"), "prueba");

        String newUserJson = writer.writeValueAsString(newUser);

        UpdateUserDTO toUpdate = new UpdateUserDTO("Jorgito", "Test", "test@email.com",
                DATE_FORMAT.parse("01/01/1999"));

        String toUpdateJson = writer.writeValueAsString(toUpdate);

        NewPasswordDTO toChangePassword = new NewPasswordDTO("test@email.com",
                "pruebas2");

        String toChangePasswordJson = writer.writeValueAsString(toChangePassword);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("User creation success"),
                        jsonPath("$.data.id").value(comparator.getId()),
                        jsonPath("$.data.name").value(comparator.getName()),
                        jsonPath("$.data.lastName").value(comparator.getLastName()),
                        jsonPath("$.data.email").value(comparator.getEmail()),
                        jsonPath("$.data.birthdate").value(comparator.getBirthdate())
                );

        comparator.setName("Jorgito");
        comparator.setLastName("Test");
        comparator.setEmail("test@email.com");
        comparator.setBirthdate("01/01/1999");

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toUpdateJson))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("User modification success"),
                        jsonPath("$.data.id").value(comparator.getId()),
                        jsonPath("$.data.name").value(comparator.getName()),
                        jsonPath("$.data.lastName").value(comparator.getLastName()),
                        jsonPath("$.data.email").value(comparator.getEmail()),
                        jsonPath("$.data.birthdate").value(comparator.getBirthdate())
                );

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/password-change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toChangePasswordJson))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("Password change success"),
                        jsonPath("$.data.id").value(comparator.getId()),
                        jsonPath("$.data.name").value(comparator.getName()),
                        jsonPath("$.data.lastName").value(comparator.getLastName()),
                        jsonPath("$.data.email").value(comparator.getEmail()),
                        jsonPath("$.data.birthdate").value(comparator.getBirthdate())
                );

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("User deletion success"),
                        jsonPath("$.data").value("User with id 1 successfully deleted")
                );

    }

}
