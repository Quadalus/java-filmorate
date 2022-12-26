package ru.yandex.practicum.filmrate.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmrate.controller.UserController;
import ru.yandex.practicum.filmrate.exception.ValidationException;
import ru.yandex.practicum.filmrate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
	private final UserController userController;

	@Autowired
	public UserControllerTest(UserController userController) {
		this.userController = userController;
	}

	@Test
	public void shouldBeNotAddUserWithWrongEmail() {
		User user = User.builder()
				.email(null)
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user1 = User.builder()
				.email("amaemail.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user2 = User.builder()
				.email("")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user3 = User.builder()
				.email(" ")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		assertThrows(ValidationException.class, () -> userController.addUser(user));
		assertThrows(ValidationException.class, () -> userController.addUser(user1));
		assertThrows(ValidationException.class, () -> userController.addUser(user2));
		assertThrows(ValidationException.class, () -> userController.addUser(user3));
	}

	@Test
	public void shouldBeNotAddUserWithWrongBirthDay() {
		User user = User.builder()
				.email("email@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.now().plusDays(1))
				.build();

		User user1 = User.builder()
				.email("amae@mail.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.MAX)
				.build();

		assertThrows(ValidationException.class, () -> userController.addUser(user));
		assertThrows(ValidationException.class, () -> userController.addUser(user1));
	}

	@Test
	public void shouldBeNotAddUserWithWrongLong() {
		User user = User.builder()
				.email("email@email.com")
				.login(null)
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user1 = User.builder()
				.email("amae@mail.com")
				.login("")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user2 = User.builder()
				.email("amae@mail.com")
				.login(" ")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user3 = User.builder()
				.email("amae@mail.com")
				.login("asdfasdf asdfasd")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		assertThrows(ValidationException.class, () -> userController.addUser(user));
		assertThrows(ValidationException.class, () -> userController.addUser(user1));
		assertThrows(ValidationException.class, () -> userController.addUser(user2));
		assertThrows(ValidationException.class, () -> userController.addUser(user3));
	}

	@Test
	public void shouldBeReplaceEmptyNameForLogin() {
		User user = User.builder()
				.email("email@email.com")
				.login("login")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user1 = User.builder()
				.email("amae@mail.com")
				.login("login")
				.name(null)
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user2 = User.builder()
				.email("amae@mail.com")
				.login("login")
				.name("")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		User user3 = User.builder()
				.email("amae@mail.com")
				.login("login")
				.name(" ")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();

		assertEquals("name", userController.addUser(user).getName());
		assertEquals("login", userController.addUser(user1).getName());
		assertEquals("login", userController.addUser(user2).getName());
		assertEquals("login", userController.addUser(user3).getName());

	}
}
