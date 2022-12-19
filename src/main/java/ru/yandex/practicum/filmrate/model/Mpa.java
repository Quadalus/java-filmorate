package ru.yandex.practicum.filmrate.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Mpa {
    private Integer id;
    private String name;
}
