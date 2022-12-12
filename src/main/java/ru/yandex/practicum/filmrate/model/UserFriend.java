package ru.yandex.practicum.filmrate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Setter
@Getter
public class UserFriend {
    private int id;
    @NotBlank
    private String status;
}
