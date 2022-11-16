package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.FilmStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            film.setId(generatedId());
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException(String.format("Невозможно обновить film c id=%d", id));
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void deleteFilm(int id) {
        films.remove(id);
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
    }

    private int generatedId() {
        return ++id;
    }
}
