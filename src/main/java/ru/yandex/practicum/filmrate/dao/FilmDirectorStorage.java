package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;

public interface FilmDirectorStorage {
	List<Film> getSortFilms(int idDirector, boolean isSortYear, boolean isSortLikes);
}