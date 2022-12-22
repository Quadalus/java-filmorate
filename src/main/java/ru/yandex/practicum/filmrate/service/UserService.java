package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.FilmStorage;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.dao.FriendStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    private final FilmStorage filmStorage;

    public List<User> findAllUser() {
        return userStorage.getAllUsers();
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

    public void addFriend(int userId, int friendId) {
        checkingUserInStorage(userId);
        checkingUserInStorage(friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        checkingUserInStorage(userId);
        checkingUserInStorage(friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getMutualFriends(int userId, int friendId) {
        checkingUserInStorage(userId);
        checkingUserInStorage(friendId);
        return friendStorage.getMutualFriends(userId, friendId)
                .stream()
                .map(userStorage.getUserMap()::get)
                .collect(Collectors.toList());
    }

    public List<User> getUserFriends(int userId) {
        checkingUserInStorage(userId);
        return friendStorage.getFriends(userId)
                .stream()
                .map(userStorage.getUserMap()::get)
                .collect(Collectors.toList());
    }

    public List<Film> getRecommendedFilms(int userId) {
        checkingUserInStorage(userId);
        return filmStorage.getRecommendedFilms(userId);
    }

    private void checkingUserInStorage(int id) {
        if (id < 0) {
            throw new NotFoundException(String.format("Пользователя с id=%d нет", id));
        }
    }
}
