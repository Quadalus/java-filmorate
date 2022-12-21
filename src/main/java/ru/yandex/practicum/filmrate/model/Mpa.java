package ru.yandex.practicum.filmrate.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Mpa {
    private Integer id;
    private String name;
}
