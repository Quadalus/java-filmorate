package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.FilmStorage;
import ru.yandex.practicum.filmrate.dao.LikeStorage;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> findAllFilms() {
        return filmStorage.getListFilms();
    }

    public Film findFilmById(int id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильма с id=%d нет", id)));
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    public void deleteFilmById(int id) {
        filmStorage.deleteFilm(id);
    }

    public void addLike(int filmId, int userId) {
        checkingFilmInStorage(filmId);
        checkingUserInStorage(userId);
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        checkingFilmInStorage(filmId);
        checkingUserInStorage(userId);
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getBestFilms(int count) {
        return likeStorage.getTopFilms(count).stream()
                .map(filmStorage.getFilmMap()::get)
                .collect(Collectors.toList());
    }

    private void checkingFilmInStorage(int filmId) {
        if (!filmStorage.getFilmMap().containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильма с id=%d нет", filmId));
        }
    }

    private void checkingUserInStorage(int userId) {
        if (!userStorage.getUserMap().containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователя с id=%d нет", userId));
        }
    }
}
