package ru.yandex.practicum.filmrate.dao;

import java.util.Set;
import java.util.List;

public interface FriendStorage {

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    Set<Integer> getFriends(int userId);

    List<Integer> getMutualFriends(int userId, int friendId);
}
