package ru.yandex.practicum.filmrate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NonNull
    @NotBlank
    private String name;
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private long duration;
}
