package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.LikeStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;

import java.util.*;

@Component
public class InMemoryLikeStorage implements LikeStorage {
    private final Map<Integer, Set<Integer>> userLikes;

    public InMemoryLikeStorage() {
        userLikes = new HashMap<>();
    }

    @Override
    public void addLike(int userId, int filmId) {
        if (!userLikes.containsKey(userId)) {
            userLikes.put(userId, new HashSet<>());
        }
        userLikes.get(userId)
                .add(filmId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        if (!userLikes.containsKey(userId)) {
            throw new NotFoundException("Такого пользователя нет.");
        }
        userLikes.get(userId)
                .remove(filmId);
    }

    @Override
    public Set<Integer> getLikedFilms(int userId) {
        if (userLikes.containsKey(userId)) {
            return userLikes.get(userId);
        }
        return Collections.emptySet();
    }
}
