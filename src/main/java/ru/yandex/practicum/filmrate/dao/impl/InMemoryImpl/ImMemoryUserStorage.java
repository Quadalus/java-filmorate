package ru.yandex.practicum.filmrate.dao.impl.InMemoryImpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.User;

import java.util.*;

@Component
public class ImMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap;
    private int id = 0;

    public ImMemoryUserStorage() {
        userMap = new HashMap<>();
    }

    @Override
    public User addUser(User user) {
        if (!userMap.containsKey(user.getId())) {
            user.setId(generatedId());
            userMap.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!userMap.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Невозможно обновить user c id=%d", id));
        }
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Map<Integer, User> getUserMap() {
        return userMap;
    }

    @Override
    public void deleteUser(int id) {
        userMap.remove(id);
    }

    @Override
    public void deleteAllUser() {
        userMap.clear();
    }

    private int generatedId() {
        return ++id;
    }
}
