package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.*;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Event;
import ru.yandex.practicum.filmrate.model.EventOperation;
import ru.yandex.practicum.filmrate.model.EventType;
import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;
	private final LikeStorage likeStorage;
	private final DirectorStorage directorStorage;
	private final FilmGenreStorage filmGenreStorage;
	private final FilmDirectorStorage filmDirectorStorage;
	private final EventStorage eventStorage;

	public Film addFilm(Film film) {
		Film newFilm = filmStorage.addFilm(film);
		addGenres(film);
		addDirectors(film);
		newFilm.setGenres(filmGenreStorage.getGenresByFilmId(newFilm.getId()));
		newFilm.setDirectors(filmDirectorStorage.getDirectorsByFilmId(newFilm.getId()));
		return newFilm;
	}

	public Film updateFilm(Film film) {
		Film updatedFilm = filmStorage.updateFilm(film);
		addGenres(film);
		addDirectors(film);
		updatedFilm.setGenres(filmGenreStorage.getGenresByFilmId(updatedFilm.getId()));
		updatedFilm.setDirectors(filmDirectorStorage.getDirectorsByFilmId(updatedFilm.getId()));
		return updatedFilm;
	}

	public List<Film> findAllFilms() {
		return filmStorage.getListFilms();
	}

	public Film findFilmById(int id) {
		Film film = filmStorage.getFilmById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Фильм с id=%d не найден", id)));
		film.setGenres(filmGenreStorage.getGenresByFilmId(film.getId()));
		film.setDirectors(filmDirectorStorage.getDirectorsByFilmId(film.getId()));
		return film;
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
		eventStorage.addEvent(Event.builder()
				.eventType(EventType.LIKE)
				.operation(EventOperation.ADD)
				.userId(userId)
				.entityId(filmId)
				.build());
	}

	public void deleteLike(int filmId, int userId) {
		checkingFilmInStorage(filmId);
		checkingUserInStorage(userId);
		likeStorage.deleteLike(filmId, userId);
		eventStorage.addEvent(Event.builder()
				.eventType(EventType.LIKE)
				.operation(EventOperation.REMOVE)
				.userId(userId)
				.entityId(filmId)
				.build());
	}

	public List<Film> getBestFilms(int count, int genreId, int year) {
		return likeStorage.getTopFilms(count, genreId, year).stream()
				.map(filmStorage.getFilmMap()::get)
				.collect(Collectors.toList());
	}

	public List<Film> getSortFilms(int idDirector, String typeSort) {
		if (directorStorage.getById(idDirector).isPresent()) {
			List<Film> films = filmDirectorStorage.getSortFilms(idDirector,
					"year".equals(typeSort), "likes".equals(typeSort));
			filmStorage.addGenresToFilms(films);
			filmStorage.addDirectorsToFilms(films);
			return films;
		} else throw new NotFoundException(String.format("Режиссер с id=%d не найден", idDirector));
	}

	public List<Film> searchFilm(String searchQuery, String searchParam) {
		return filmStorage.searchFilm(searchQuery, searchParam);
	}

	public List<Film> getCommonFilms(int userId, int friendId) {
		return filmStorage.getCommonFilms(userId, friendId);
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

	private void addDirectors(Film film) {
		if (film.getDirectors() != null) {
			filmDirectorStorage.addDirectorsToFilm(film.getId(), film.getDirectors());
		}
	}

	private void addGenres(Film film) {
		if (film.getGenres() != null) {
			filmGenreStorage.addGenreToFilm(film.getId(), film.getGenres());
		}
	}
}
