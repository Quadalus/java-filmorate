package ru.yandex.practicum.filmrate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private long duration;
}
