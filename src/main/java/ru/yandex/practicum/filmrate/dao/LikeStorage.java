package ru.yandex.practicum.filmrate.dao;

import java.util.List;

public interface LikeStorage {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Integer> getTopFilms(int count);

    void addFilmToRating(int filmId);

    void deleteFilmFromRating(int filmId);
}
