package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.User;

import java.util.*;

@Component
public class ImMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private int id = 0;

    public ImMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public Optional<User> addUser(User user) {
        Optional<User> userOptional = Optional.ofNullable(user);
        if (userOptional.isPresent() && !users.containsKey(user.getId())) {
            user.setId(generatedId());
            users.put(user.getId(), user);
        }
        return userOptional;
    }

    @Override
    public Optional<User> updateUser(User user) {
        Optional<User> userOptional = Optional.ofNullable(user);
        if (userOptional.isEmpty() || !users.containsKey(user.getId())) {
            throw new NotFoundException("Невозможно обновить user");
        }
        users.put(user.getId(), user);
        return userOptional;
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(users.get(id));
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
