package ru.yandex.practicum.filmrate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.exception.ValidationException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final static int MAX_DESCRIPTION_LENGTH = 200;
    private final static LocalDate OLDEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            validateFilm(film);
            film.setId(generatedId());
            films.put(film.getId(), film);
            log.debug("Фильм добавлен.");
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Указан неверный id={}", film.getId());
            throw new ValidationException("Фильма с таким id нет.");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.debug("Фильм с Id={} обновлён.", film.getId());
        return film;
    }

    @Override
    public List<Film> findAllFilms() {
        log.debug("Список фильмов получен");
        return new ArrayList<>(films.values());
    }

    @Override
    public void deleteFilm(int id) {
        log.debug("Фильм с Id={} удалён.", id);
        films.remove(id);
    }

    @Override
    public void deleteAllFilms() {
        log.info("Список фильмов очищен.");
        films.clear();
    }

    private int generatedId() {
        return ++id;
    }

    private void validateFilm(Film film) {
        if (StringUtils.isBlank(film.getName())) {
            log.warn("Не было указано название фильма.");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(OLDEST_RELEASE_DATE)) {
            log.warn("Самая ранняя возможная дата релиза фильма {}, указанная дата={}", OLDEST_RELEASE_DATE,
                    film.getReleaseDate());
            throw new ValidationException("Указано неверное начало фильма");
        }

        if (film.getDuration() < 0) {
            log.warn("Длительность фильма не может быть отрицательной, указанная длительность={}", film.getDuration());
            throw new ValidationException("Указана неверная длительность фильма ");
        }

        if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Максимальная длинна описания фильма равна={}, указанное описание={}", MAX_DESCRIPTION_LENGTH,
                    film.getDescription().length());
            throw new ValidationException("Описание фильма слишком длинное");
        }
    }
}
