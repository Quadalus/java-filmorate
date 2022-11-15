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
    public Optional<Film> addFilm(Film film) {
        Optional<Film> optionalFilm = Optional.ofNullable(film);
        if (optionalFilm.isPresent() && !films.containsKey(film.getId())) {
            film.setId(generatedId());
            films.put(film.getId(), film);
        }
        return optionalFilm;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        Optional<Film> optionalFilm = Optional.ofNullable(film);
        if (optionalFilm.isEmpty() || !films.containsKey(film.getId())) {
            throw new NotFoundException("Такого фильма нет.");
        }
        films.put(film.getId(), film);
        return optionalFilm;
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
