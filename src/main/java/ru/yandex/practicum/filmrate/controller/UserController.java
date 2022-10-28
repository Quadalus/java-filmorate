package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private int id = 0;
    private final List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("абоба");
        return users;
    }

    @PostMapping("/user")
    public void add(@Valid @RequestBody User user) {
        user.setId(generatedId());
        log.info("вова");
        users.add(user);
    }

    @PutMapping("/user")
    public void updateOrAdd(@Valid @RequestBody User user) {
        log.info("dfs");
        users.add(user);
    }

    private int generatedId() {
        return ++id;
    }
}
