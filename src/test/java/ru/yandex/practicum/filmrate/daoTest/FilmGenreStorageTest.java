package ru.yandex.practicum.filmrate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmrate.dao.FilmGenreStorage;
import ru.yandex.practicum.filmrate.dao.FilmStorage;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.Genre;
import ru.yandex.practicum.filmrate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreStorageTest {
	private final FilmStorage filmStorage;
	private final FilmGenreStorage filmGenreStorage;

	@Test
	void createFilmWithGenre() {
		Film testFilm = Film.builder()
				.name("Test Film Name")
				.description("test film 1")
				.releaseDate(LocalDate.of(2022, 1, 1))
				.duration(100)
				.rate(4)
				.mpa(new Mpa(1, "R"))
				.genres(new LinkedHashSet<>(Set.of(new Genre(1, "Комедия"))))
				.build();

		filmStorage.addFilm(testFilm);
		filmGenreStorage.addGenreToFilm(testFilm.getId(), testFilm.getGenres());
		var filmGenres = filmGenreStorage.getGenresByFilmId(testFilm.getId());
		var genres = Set.of(new Genre(1, "Комедия"));

		assertEquals(1, testFilm.getGenres().size());
		assertEquals(genres, filmGenres);
	}

	@Test
	void updateFilmWithGenre() {
		Film testFilm = Film.builder()
				.id(1)
				.name("Test Film Name")
				.description("test film 1")
				.releaseDate(LocalDate.of(2022, 1, 1))
				.duration(100)
				.rate(4)
				.mpa(new Mpa(1, "R"))
				.genres(new LinkedHashSet<>(Set.of(new Genre(1, "Комедия"))))
				.build();

		Film film = filmStorage.addFilm(testFilm);
		filmGenreStorage.addGenreToFilm(testFilm.getId(), testFilm.getGenres());
		var filmGenres = filmGenreStorage.getGenresByFilmId(testFilm.getId());
		var genres = Set.of(new Genre(1, "Комедия"));

		assertEquals(1, testFilm.getGenres().size());
		assertEquals(genres, filmGenres);

		Film updatedFilm = Film.builder()
				.id(film.getId())
				.name("Test Film Name")
				.description("test film 1")
				.releaseDate(LocalDate.of(2022, 1, 1))
				.duration(100)
				.rate(4)
				.mpa(new Mpa(1, "R"))
				.genres(new LinkedHashSet<>(Set.of(new Genre(1, "Комедия"), new Genre(2, "Боевик"))))
				.build();

		filmStorage.updateFilm(updatedFilm);
		testFilm = filmStorage.getFilmById(film.getId()).get();
		filmGenreStorage.addGenreToFilm(updatedFilm.getId(), updatedFilm.getGenres());
		filmGenres = filmGenreStorage.getGenresByFilmId(updatedFilm.getId());
		testFilm.setGenres(filmGenres);
		genres = Set.of(new Genre(1, "Комедия"), new Genre(2, "Боевик"));

		assertEquals(film.getId(), testFilm.getId());
		assertEquals(1, filmStorage.getListFilms().size());
		assertEquals(2, testFilm.getGenres().size());
		assertEquals(genres, filmGenres);
	}
}
