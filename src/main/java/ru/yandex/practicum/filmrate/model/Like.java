package ru.yandex.practicum.filmrate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Like {
    private int filmId;
    private int userId;
}
