package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Genre;

import java.util.Set;

public interface FilmGenreStorage {
    Set<Genre> getGenresByFilmId(int filmId);

    void addGenreToFilm(int filmId, Set<Genre> genreList);

    void deleteGenresByFilmId(int filmId);
}
