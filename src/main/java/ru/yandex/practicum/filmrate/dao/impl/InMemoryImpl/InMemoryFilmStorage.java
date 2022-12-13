package ru.yandex.practicum.filmrate.dao.impl.InMemoryImpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.FilmStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> filmMap;

    public InMemoryFilmStorage() {
        filmMap = new HashMap<>();
    }

    @Override
    public Film addFilm(Film film) {
        if (!filmMap.containsKey(film.getId())) {
            film.setId(generatedId());
            filmMap.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!filmMap.containsKey(film.getId())) {
            throw new NotFoundException(String.format("Невозможно обновить film c id=%d", id));
        }
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.ofNullable(filmMap.get(id));
    }

    @Override
    public List<Film> getListFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Map<Integer, Film> getFilmMap() {
        return filmMap;
    }

    @Override
    public void deleteFilm(int id) {
        filmMap.remove(id);
    }

    @Override
    public void deleteAllFilms() {
        filmMap.clear();
    }

    private int generatedId() {
        return ++id;
    }
}
