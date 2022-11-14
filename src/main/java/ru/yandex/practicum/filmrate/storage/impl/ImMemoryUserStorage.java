package ru.yandex.practicum.filmrate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.exception.ValidationException;
import ru.yandex.practicum.filmrate.model.User;
import ru.yandex.practicum.filmrate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ImMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private int id = 0;

    public ImMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public User addUser(User user) {
        if (!users.containsKey(user.getId())) {
            validateUser(user);
            user.setId(generatedId());
            users.put(user.getId(), user);
            log.debug("Пользователь создан.");
        }
        return null;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Указан несуществующий ID={}", user.getId());
            throw new ValidationException("Невозможно обновить user");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.debug("Пользователь с Id={} обновлён.", user.getId());
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        log.info("Список пользователей получен.");
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
        log.debug("Пользователь с Id={} удален.", id);
    }

    @Override
    public void deleteAllUser() {
        users.clear();
        log.info("Все пользователи удалены.");
    }

    private int generatedId() {
        return ++id;
    }

    private void validateUser(User user) {
        if (StringUtils.isBlank(user.getEmail()) || StringUtils.containsNone(user.getEmail(), "@")) {
            log.warn("Указан неверный формат email, указан email={}.", user.getEmail());
            throw new ValidationException("Указан неверный email.");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем. Сейчас {}, указанная дата {}", LocalDate.now(),
                    user.getBirthday());
            throw new ValidationException("Указана неверная дата рождения");
        }

        if (StringUtils.isBlank(user.getLogin()) || StringUtils.containsWhitespace(user.getLogin())) {
            log.warn("Логин не может содержать пробелы или быть пустым, указанный логин={}.", user.getLogin());
            throw new ValidationException("Указан неверный логин.");
        }

        if (StringUtils.isBlank(user.getName())) {
            log.debug("Имя не установлено, имя взято из логина. Установлено имя={} ", user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
