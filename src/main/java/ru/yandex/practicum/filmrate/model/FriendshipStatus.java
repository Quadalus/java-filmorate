package ru.yandex.practicum.filmrate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FriendshipStatus {
    private int userId;
    private int friendId;
    private int statusId;
}
