package ru.yandex.practicum.filmrate.dao;

import java.util.List;
import java.util.Set;

public interface FriendStorage {

	void addFriend(int id, int friendId);

	void deleteFriend(int id, int friendId);

	Set<Integer> getFriends(int userId);

	List<Integer> getMutualFriends(int userId, int friendId);
}
