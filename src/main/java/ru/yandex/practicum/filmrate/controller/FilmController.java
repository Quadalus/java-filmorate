package ru.yandex.practicum.filmrate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.service.FilmService;
import ru.yandex.practicum.filmrate.service.ValidationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final ValidationService validationService;

    /*@Autowired
    public FilmController(FilmService filmService, ValidationService validationService) {
        this.filmService = filmService;
        this.validationService = validationService;
    }*/

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findAllFilms() {
        log.info("Список фильмов получен.");
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findFilm(@PathVariable int id) {
        Film foundFilm = filmService.findFilmById(id);
        log.info("Фильм с id={} получен", foundFilm.getId());
        return foundFilm;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        validationService.validateFilm(film);
        Film addedFilm = filmService.addFilm(film);
        log.info("Фильм с id={} добавлен.", addedFilm.getId());
        return addedFilm;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        validationService.validateFilm(film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Фильм с id={} обновлён.", updatedFilm.getId());
        return updatedFilm;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllFilms() {
        log.info("Все фильмы были удалены.");
        filmService.deleteAllFilms();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable int id) {
        log.info("Фильм с id={} был удалён", id);
        filmService.deleteFilmById(id);
    }
}
