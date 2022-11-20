package ru.yandex.practicum.filmrate.dao.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.dao.FriendStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFriendStorage implements FriendStorage {
    private final Map<Integer, Set<Integer>> usersFriends;

    public InMemoryFriendStorage() {
        this.usersFriends = new HashMap<>();
    }

    @Override
    public void addFriend(int userId, int friendId) {
        checkingExistenceUser(userId);
        checkingExistenceUser(friendId);
        addToFriendship(userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (!usersFriends.containsKey(userId) || !usersFriends.containsKey(friendId)) {
            throw new NotFoundException("Такого пользователя нет.");
        }
        removeFromFriendship(userId, friendId);
    }

    @Override
    public Set<Integer> getFriends(int userId) {
        if (usersFriends.containsKey(userId)) {
            return usersFriends.get(userId);
        }
        return Collections.emptySet();
    }

    @Override
    public List<Integer> getMutualFriends(int userId, int friendId) {
        var friends = getFriends(userId);
        var otherFriends = getFriends(friendId);

        return friends.stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }

    private void addToFriendship(int userId, int friendId) {
        usersFriends.get(userId).add(friendId);
        usersFriends.get(friendId).add(userId);
    }

    private void removeFromFriendship(int userId, int friendId) {
        usersFriends.get(userId).remove(friendId);
        usersFriends.get(friendId).remove(userId);
    }

    private void checkingExistenceUser(int userId) {
        if (!usersFriends.containsKey(userId)) {
            usersFriends.put(userId, new HashSet<>());
        }
    }
}
