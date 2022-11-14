package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.User;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    /*@GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }*/

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return user;
    }

    @PutMapping
    public User updateOrAddUser(@Valid @RequestBody User user) {
        return user;
    }
}