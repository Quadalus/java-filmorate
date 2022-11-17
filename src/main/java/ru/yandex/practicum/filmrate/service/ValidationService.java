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
            //log.warn("Указан неверный формат email, указан email={}.", user.getEmail());
            throw new ValidationException("Указан неверный email.");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            //log.warn("Дата рождения не может быть в будущем. Сейчас {}, указанная дата {}", LocalDate.now(),
            //user.getBirthday());
            throw new ValidationException("Указана неверная дата рождения");
        }

        if (StringUtils.isBlank(user.getLogin()) || StringUtils.containsWhitespace(user.getLogin())) {
            //log.warn("Логин не может содержать пробелы или быть пустым, указанный логин={}.", user.getLogin());
            throw new ValidationException("Указан неверный логин.");
        }

        if (StringUtils.isBlank(user.getName())) {
            //log.debug("Имя не установлено, имя взято из логина. Установлено имя={} ", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    public void validateFilm(Film film) {
        if (StringUtils.isBlank(film.getName())) {
            //log.warn("Не было указано название фильма.");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(OLDEST_RELEASE_DATE)) {
/*            log.warn("Самая ранняя возможная дата релиза фильма {}, указанная дата={}", OLDEST_RELEASE_DATE,
                    film.getReleaseDate())*/
            ;
            throw new ValidationException("Указано неверное начало фильма");
        }

        if (film.getDuration() < 0) {
//            log.warn("Длительность фильма не может быть отрицательной, указанная длительность={}", film.getDuration());
            throw new ValidationException("Указана неверная длительность фильма ");
        }

        if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
//            log.warn("Максимальная длинна описания фильма равна={}, указанное описание={}", MAX_DESCRIPTION_LENGTH,
            //film.getDescription().length());
            throw new ValidationException("Описание фильма слишком длинное");
        }
    }

    public void validateCount(int count) {
        if (count <= 0) {
            throw new ValidationException("Значение count должно быть больше нуля.");
        }
    }
}

