package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    void deleteFilm(int id);

    void deleteAllFilms();

    Optional<Film> getFilmById(int id);

    List<Film> getAllFilms();
}
