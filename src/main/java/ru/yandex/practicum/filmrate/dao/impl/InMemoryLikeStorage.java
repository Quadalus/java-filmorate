package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.LikeStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryLikeStorage implements LikeStorage {
    private final Map<Integer, Set<Integer>> likedFilms;

    public InMemoryLikeStorage() {
        likedFilms = new HashMap<>();
    }

    @Override
    public void addLike(int filmId, int userId) {
        likedFilms.get(filmId).add(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        if (!likedFilms.containsKey(filmId)) {
            throw new NotFoundException("Такого фильма нет.");
        }
        likedFilms.get(filmId).remove(userId);
    }

    @Override
    public List<Integer> getTopFilms(int count) {
        return likedFilms.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(((o1, o2) -> Integer.compare(o2.size(), o1.size()))))
                .map((Map.Entry::getKey))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addFilmToRating(int filmId) {
        if (!likedFilms.containsKey(filmId)) {
            likedFilms.put(filmId, new HashSet<>());
        }
    }

    @Override
    public void deleteFilmFromRating(int filmId) {
        likedFilms.remove(filmId);
    }
}
