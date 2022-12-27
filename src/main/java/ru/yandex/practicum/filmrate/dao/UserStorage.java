package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
	User addUser(User user);

	User updateUser(User user);

	void deleteUser(int id);

	void deleteAllUser();

	Optional<User> getUserById(int id);

	List<User> getAllUsers();

	Map<Integer, User> getUserMap();
}
