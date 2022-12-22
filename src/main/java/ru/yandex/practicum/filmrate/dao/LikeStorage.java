package ru.yandex.practicum.filmrate.dao;

import java.util.List;

public interface LikeStorage {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Integer> getTopFilms(int count, int genreId, int year);
}
