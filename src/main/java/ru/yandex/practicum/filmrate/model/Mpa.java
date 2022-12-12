package ru.yandex.practicum.filmrate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Mpa {
    private int id;
    @NotBlank
    private String name;
}
