package ru.yandex.practicum.filmrate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    private Integer id;
    @Email
    private String email;
    @NotBlank
    private String login;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate birthday;
}
