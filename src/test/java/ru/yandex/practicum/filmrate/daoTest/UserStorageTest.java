package ru.yandex.practicum.filmrate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserStorageTest {
	private final UserStorage userStorage;

	@Test
	void addUser() {
		User user = User.builder()
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);

		User userFromStorage = userStorage.getUserById(1).get();
		assertEquals("nick@email.com", userFromStorage.getEmail());
		assertEquals("nick", userFromStorage.getLogin());
		assertEquals("name", userFromStorage.getName());
		assertEquals("1990-10-04", userFromStorage.getBirthday().toString());
	}

	@Test
	void updateUser() {
		User user = User.builder()
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);

		User userFromStorage = userStorage.getUserById(1).get();
		assertEquals("nick@email.com", userFromStorage.getEmail());
		assertEquals("nick", userFromStorage.getLogin());
		assertEquals("name", userFromStorage.getName());
		assertEquals("1990-10-04", userFromStorage.getBirthday().toString());

		User updatedUser = User.builder()
				.id(1)
				.email("updatednick@email.com")
				.login("Updated nick")
				.name("Updated Name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.updateUser(updatedUser);

		userFromStorage = userStorage.getUserById(1).get();
		assertEquals("updatednick@email.com", userFromStorage.getEmail());
		assertEquals("Updated nick", userFromStorage.getLogin());
		assertEquals("Updated Name", userFromStorage.getName());
	}

	@Test
	void deleteUser() {
		User user = User.builder()
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);
		assertEquals(1, userStorage.getAllUsers().size());

		userStorage.deleteUser(1);
		assertEquals(0, userStorage.getAllUsers().size());
	}

	@Test
	void deleteAllUser() {
		User user = User.builder()
				.id(1)
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);

		User user2 = User.builder()
				.id(2)
				.email("nick2@email.com")
				.login("nick2")
				.name("name2")
				.birthday(LocalDate.of(1994, 10, 4))
				.build();
		userStorage.addUser(user2);
		assertEquals(2, userStorage.getAllUsers().size());

		userStorage.deleteAllUser();
		assertEquals(0, userStorage.getAllUsers().size());
	}

	@Test
	void getUserById() {
		User user = User.builder()
				.id(1)
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);

		User userFromStorage = userStorage.getUserById(1).get();
		assertEquals("nick@email.com", userFromStorage.getEmail());
		assertEquals("nick", userFromStorage.getLogin());
		assertEquals("name", userFromStorage.getName());
		assertEquals("1990-10-04", userFromStorage.getBirthday().toString());
	}

	@Test
	void getAllUsers() {
		User user = User.builder()
				.id(1)
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);

		User user2 = User.builder()
				.id(2)
				.email("nick2@email.com")
				.login("nick2")
				.name("name2")
				.birthday(LocalDate.of(1994, 10, 4))
				.build();
		userStorage.addUser(user2);
		assertEquals(2, userStorage.getAllUsers().size());
	}
}
