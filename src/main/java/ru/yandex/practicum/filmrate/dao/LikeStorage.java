package ru.yandex.practicum.filmrate.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LikeStorage {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Map<Integer, Set<Integer>> getLikedFilms();

    List<Integer> getTopFilms(int count);
}
