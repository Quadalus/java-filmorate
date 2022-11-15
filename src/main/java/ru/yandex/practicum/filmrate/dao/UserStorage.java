package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);

    void deleteUser(int id);

    void deleteAllUser();

    Optional<User> getUserById(int id);

    List<User> findAllUsers();
}
