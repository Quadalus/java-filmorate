package ru.yandex.practicum.filmrate.storage;

import ru.yandex.practicum.filmrate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    void deleteAllUser();

    List<User> findAllUsers();
}
