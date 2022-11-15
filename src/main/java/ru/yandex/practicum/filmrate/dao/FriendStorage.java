package ru.yandex.practicum.filmrate.dao;

import java.util.Set;

public interface FriendStorage {

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    Set<Integer> getUserFriends(int userId);
}
