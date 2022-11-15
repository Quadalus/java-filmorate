package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.FriendStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;

import java.util.*;

@Component
public class InMemoryFriendStorage implements FriendStorage {
    private final Map<Integer, Set<Integer>> userFriends;

    public InMemoryFriendStorage() {
        this.userFriends = new HashMap<>();
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (!userFriends.containsKey(userId)) {
            userFriends.put(userId, new HashSet<>());
        }
        userFriends.get(userId)
                .add(friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (!userFriends.containsKey(userId)) {
            throw new NotFoundException("Такого пользователя нет.");
        }
        userFriends.get(userId)
                .remove(friendId);
    }

    @Override
    public Set<Integer> getUserFriends(int userId) {
        if (userFriends.containsKey(userId)) {
            return userFriends.get(userId);
        }
        return Collections.emptySet();
    }
}
