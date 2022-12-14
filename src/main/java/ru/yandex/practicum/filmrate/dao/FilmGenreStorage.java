package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {
    List<Genre> getGenresByFilmId(int filmId);

    void addGenreToFilm(int filmId, List<Integer> genreList);

    void deleteGenresByFilmId(int filmId);
}
