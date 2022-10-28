package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.exception.ControllerValidationException;
import ru.yandex.practicum.filmrate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final static int MAX_DESCRIPTION_LENGTH = 200;
    private final static LocalDate OLDEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public Collection<Film> findAllFilms() {
        log.debug("Список фильмов получен");
        return films.values();
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            validateFilm(film);
            film.setId(generatedId());
            films.put(film.getId(), film);
            log.debug("Фильм добавлен.");
        }
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Указан неверный id={}", film.getId());
            throw new ControllerValidationException("Фильма с таким id нет.");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.debug("Фильм с Id={} обновлён.", film.getId());
        return film;
    }

    private int generatedId() {
        return ++id;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(OLDEST_RELEASE_DATE)) {
            log.warn("Самая ранняя возможная дата релиза фильма {}, указана {}", OLDEST_RELEASE_DATE,
                    film.getReleaseDate());
            throw new ControllerValidationException("Указано неверное начало фильма");
        }

        if (film.getDuration() < 0) {
            log.warn("Длительность фильма не может быть отрицательной, указана={}", film.getDuration());
            throw new ControllerValidationException("Указана неверная длительность фильма ");
        }

        if (film.getDescription().length() >= MAX_DESCRIPTION_LENGTH) {
            log.warn("Максимальная длинна фильма равна={}, указана={}", MAX_DESCRIPTION_LENGTH,
                    film.getDescription().length());
            throw new ControllerValidationException("Описание фильма слишком длинное");
        }
    }
}
