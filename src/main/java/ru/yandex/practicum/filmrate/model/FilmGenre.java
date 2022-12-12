package ru.yandex.practicum.filmrate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class FilmGenre {
    private int filmId;
    private int genreId;
}
