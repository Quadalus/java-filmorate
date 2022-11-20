package ru.yandex.practicum.filmrate.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.exception.ValidationException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.User;

import java.time.LocalDate;

@Service
public class ValidationService {
    private final static int MAX_DESCRIPTION_LENGTH = 200;
    private final static LocalDate OLDEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void validateUser(User user) {
        if (StringUtils.isBlank(user.getEmail()) || StringUtils.containsNone(user.getEmail(), "@")) {
            throw new ValidationException("Указан неверный email.");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Указана неверная дата рождения");
        }

        if (StringUtils.isBlank(user.getLogin()) || StringUtils.containsWhitespace(user.getLogin())) {
            throw new ValidationException("Указан неверный логин.");
        }

        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
    }

    public void validateFilm(Film film) {
        if (StringUtils.isBlank(film.getName())) {
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(OLDEST_RELEASE_DATE)) {
            throw new ValidationException("Указано неверное начало фильма");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Указана неверная длительность фильма ");
        }

        if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Описание фильма слишком длинное");
        }
    }

    public void validateCount(int count) {
        if (count <= 0) {
            throw new ValidationException("Значение count должно быть больше нуля.");
        }
    }
}

