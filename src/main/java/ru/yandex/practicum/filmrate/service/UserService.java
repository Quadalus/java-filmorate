package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
//    private FriendStorage FriendStorage;

    public List<User> findAllUser() {
        return userStorage.findAllUsers();
    }

    public User findUserById(int id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Невозможно получить пользователя с id=%d", id)));
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUser();
    }

    public void deleteUserById(int id) {
        userStorage.deleteUser(id);
    }
}
