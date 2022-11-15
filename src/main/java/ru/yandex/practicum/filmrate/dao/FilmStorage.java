package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);

    void deleteAllFilms();

    List<Film> findAllFilms();
}
