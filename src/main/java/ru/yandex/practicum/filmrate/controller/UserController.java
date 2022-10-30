package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.exception.ControllerValidationException;
import ru.yandex.practicum.filmrate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Список пользователей получен.");
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            validateUser(user);
            user.setId(generatedId());
            users.put(user.getId(), user);
            log.debug("Пользователь создан.");
        }
        return user;
    }

    @PutMapping
    public User updateOrAddUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Указан несуществующий ID={}", user.getId());
            throw new ControllerValidationException("Невозможно обновить user");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.debug("Пользователь с Id={} обновлён.", user.getId());
        return user;
    }

    private int generatedId() {
        return ++id;
    }

    private void validateUser(User user) {
        if (StringUtils.isBlank(user.getEmail()) || StringUtils.containsNone(user.getEmail(), "@")) {
            log.warn("Указан неверный формат email, указан email={}.", user.getEmail());
            throw new ControllerValidationException("Указан неверный email.");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем. Сейчас {}, указанная дата {}", LocalDate.now(),
                    user.getBirthday());
            throw new ControllerValidationException("Указана неверная дата рождения");
        }

        if (StringUtils.isBlank(user.getLogin()) || StringUtils.containsWhitespace(user.getLogin())) {
            log.warn("Логин не может содержать пробелы или быть пустым, указанный логин={}.", user.getLogin());
            throw new ControllerValidationException("Указан неверный логин.");
        }

        if (StringUtils.isBlank(user.getName())) {
            log.debug("Имя не установлено, имя взято из логина. Установлено имя={} ", user.getLogin());
            user.setName(user.getLogin());
        }
    }
}