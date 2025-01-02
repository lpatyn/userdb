package com.users.userdb;

import com.users.userdb.dto.request.NewUserDTO;
import com.users.userdb.dto.response.UserDTO;
import com.users.userdb.entities.User;
import com.users.userdb.exceptions.UserNotFoundException;
import com.users.userdb.repositories.interfaces.IUserRepository;
import com.users.userdb.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.users.userdb.constants.ApplicationConstants.DATE_FORMAT;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MockedUserServiceTest {

	@Mock
	private IUserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void findAllTest() throws Exception {
		List<User> returnedUsers = new ArrayList<>();
		returnedUsers.add(new User(1, "Jorge", "Pruebas", "jorge@email.com",
				DATE_FORMAT.parse("01/01/1990"), "prueba"));
		returnedUsers.add(new User(2, "Test", "Pruebas", "test@email.com",
				DATE_FORMAT.parse("15/01/1990"), "prueba"));
		returnedUsers.add(new User(3, "Robert", "Pruebas", "robert@email.com",
				DATE_FORMAT.parse("01/12/1990"), "prueba"));
		returnedUsers.add(new User(4, "George", "Testing", "george@email.com",
				DATE_FORMAT.parse("20/10/1990"), "prueba"));

		List<UserDTO> expectedUsers = new ArrayList<>();
		expectedUsers.add(new UserDTO(1, "Jorge", "Pruebas", "jorge@email.com",
				"01/01/1990"));
		expectedUsers.add(new UserDTO(2, "Test", "Pruebas", "test@email.com",
				"15/01/1990"));
		expectedUsers.add(new UserDTO(3, "Robert", "Pruebas", "robert@email.com",
				"01/12/1990"));
		expectedUsers.add(new UserDTO(4, "George", "Testing", "george@email.com",
				"20/10/1990"));

		when(userRepository.findAll()).thenReturn(returnedUsers);
		List<UserDTO> obtainedUsers = userService.findAll();

		assertEquals(expectedUsers.size(), obtainedUsers.size());
		for (int i = 0; i < expectedUsers.size(); i++) {
			assertEquals(expectedUsers.get(i).getId(), obtainedUsers.get(i).getId());
			assertEquals(expectedUsers.get(i).getName(), obtainedUsers.get(i).getName());
			assertEquals(expectedUsers.get(i).getLastName(), obtainedUsers.get(i).getLastName());
			assertEquals(expectedUsers.get(i).getEmail(), obtainedUsers.get(i).getEmail());
			assertEquals(expectedUsers.get(i).getBirthdate(), obtainedUsers.get(i).getBirthdate());
		}

	}

	@Test
	void findByIdTest() throws Exception {
		Optional<User> foundOptional = Optional.of(new User(1, "Jorge", "Pruebas",
				"jorge@email.com", DATE_FORMAT.parse("01/01/1990"), "prueba"));

		UserDTO expected = new UserDTO(1, "Jorge", "Pruebas", "jorge@email.com",
				"01/01/1990");

		when(userRepository.findById(1)).thenReturn(foundOptional);
		UserDTO foundUser = userService.findById(1);

		try {
			UserDTO notFound = userService.findById(15);
		} catch (UserNotFoundException e) {
			assertEquals("Requested user not found",e.getMessage());
			assertEquals("id",e.getKey());
			assertEquals("15",e.getValue());
		}

		assertAll(() -> {
			assertEquals(expected.getId(), foundUser.getId());
			assertEquals(expected.getName(), foundUser.getName());
			assertEquals(expected.getLastName(), foundUser.getLastName());
			assertEquals(expected.getEmail(), foundUser.getEmail());
			assertEquals(expected.getBirthdate(), foundUser.getBirthdate());
		});
	}
}
