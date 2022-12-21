package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
	Film addFilm(Film film);

	Film updateFilm(Film film);

	void deleteFilm(int id);

	void deleteAllFilms();

	Optional<Film> getFilmById(int id);

	List<Film> getListFilms();

	Map<Integer, Film> getFilmMap();

	void addGenresToFilms(List<Film> films);

	void addDirectorsToFilms(List<Film> films);
    Map<Integer, Film> getFilmMap();

    List<Film> getRecommendedFilms(int userId);
}
