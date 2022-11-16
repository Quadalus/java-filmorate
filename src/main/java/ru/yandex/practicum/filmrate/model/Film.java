package ru.yandex.practicum.filmrate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    private int id;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @PositiveOrZero
    private long duration;
}
