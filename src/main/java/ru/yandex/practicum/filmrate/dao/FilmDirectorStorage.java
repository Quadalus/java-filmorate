package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Director;
import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmDirectorStorage {

	Set<Director> getDirectorsByFilmId(Integer id);

	List<Film> getSortFilms(int idDirector, boolean isSortYear, boolean isSortLikes);

	void addDirectorsToFilm(int filmId, Set<Director> directors);

	void deleteDirectorsByFilmId(int filmId);
}