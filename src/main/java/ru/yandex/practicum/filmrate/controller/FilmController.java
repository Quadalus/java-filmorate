package ru.yandex.practicum.filmrate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.service.FilmService;
import ru.yandex.practicum.filmrate.service.ValidationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
	private final FilmService filmService;
	private final ValidationService validationService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Film> findAllFilms() {
		log.info("Список фильмов получен.");
		return filmService.findAllFilms();
	}

	@GetMapping("/director/{directorId}")
	@ResponseStatus(HttpStatus.OK)
	public List<Film> findSortFilms(@PathVariable int directorId,
	                                @RequestParam(defaultValue = "year") String sortBy) {
		log.info("Список фильмов получен.");
		return filmService.getSortFilms(directorId, sortBy);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Film findFilm(@PathVariable int id) {
		Film foundFilm = filmService.findFilmById(id);
		log.info("Фильм с id={} получен", foundFilm.getId());
		return foundFilm;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Film addFilm(@Valid @RequestBody Film film) {
		validationService.validateFilm(film);
		Film addedFilm = filmService.addFilm(film);
		log.info("Фильм с id={} добавлен.", addedFilm.getId());
		return addedFilm;
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Film updateFilm(@Valid @RequestBody Film film) {
		validationService.validateFilm(film);
		Film updatedFilm = filmService.updateFilm(film);
		log.info("Фильм с id={} обновлён.", updatedFilm.getId());
		return updatedFilm;
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAllFilms() {
		log.info("Все фильмы были удалены.");
		filmService.deleteAllFilms();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFilm(@PathVariable int id) {
		log.info("Фильм с id={} был удалён", id);
		filmService.deleteFilmById(id);
	}

	@PutMapping("/{id}/like/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void addLike(@PathVariable int id,
	                    @PathVariable int userId) {
		log.info("Пользователь с id={} добавил лайк фильму с id={}.", userId, id);
		filmService.addLike(id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLike(@PathVariable int id,
	                       @PathVariable int userId) {
		log.info("У фильма с id={} удалён лайк пользователем с userId={}", id, userId);
		filmService.deleteLike(id, userId);
	}

	@GetMapping("/popular")
	@ResponseStatus(HttpStatus.OK)
	public List<Film> getTopFilms(@RequestParam(required = false, defaultValue = "10") int count,
								  @RequestParam(required = false, defaultValue = "0") int genreId,
								  @RequestParam(required = false, defaultValue = "0") int year) {
		validationService.validateCount(count);
		log.info("Список популярных фильмов получен.");
		return filmService.getBestFilms(count, genreId, year);
	}
}
