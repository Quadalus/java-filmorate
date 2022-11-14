package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.Film;

import javax.validation.Valid;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    /*@GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }*/

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return film;
    }
}
