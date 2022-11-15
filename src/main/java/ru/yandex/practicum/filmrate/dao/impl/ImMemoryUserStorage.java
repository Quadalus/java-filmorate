package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ImMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private int id = 0;

    public ImMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public User addUser(User user) {
        if (!users.containsKey(user.getId())) {
            user.setId(generatedId());
            users.put(user.getId(), user);
        }
        return null;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Невозможно обновить user");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    @Override
    public void deleteAllUser() {
        users.clear();
    }

    private int generatedId() {
        return ++id;
    }
}
