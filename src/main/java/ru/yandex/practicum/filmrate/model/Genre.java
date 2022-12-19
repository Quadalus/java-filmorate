package ru.yandex.practicum.filmrate.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Genre {
    private Integer id;
    private String name;
}
