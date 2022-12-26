package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmrate.dao.DirectorStorage;
import ru.yandex.practicum.filmrate.model.Director;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceTest {

	private final FilmService filmService;
	private final DirectorStorage directorStorage;

	Film testFilm1 = Film.builder()
			.name("Test Film Name 1")
			.description("test film 1")
			.releaseDate(LocalDate.of(2000, 1, 1))
			.duration(100)
			.rate(3)
			.mpa(new Mpa(1, "R"))
			.build();

	Film testFilm2 = Film.builder()
			.name("Test Film Name 2")
			.description("test film 2")
			.releaseDate(LocalDate.of(1990, 1, 1))
			.duration(100)
			.rate(4)
			.mpa(new Mpa(1, "R"))
			.build();

	Film testFilm3 = Film.builder()
			.name("Test Film Name 3")
			.description("test film 3")
			.releaseDate(LocalDate.of(1999, 1, 1))
			.duration(100)
			.rate(2)
			.mpa(new Mpa(1, "R"))
			.build();

	Director director1 = new Director("Director1");
	Director director2 = new Director("Director2");

	@Test
	void checkSortYear() {
		Director createdDirector1 = directorStorage.createDirector(director1);
		Director createdDirector2 = directorStorage.createDirector(director2);
		testFilm1.setDirectors(new HashSet<>(List.of(createdDirector1, createdDirector2)));
		testFilm2.setDirectors(new HashSet<>(List.of(createdDirector1)));
		testFilm3.setDirectors(new HashSet<>(List.of(createdDirector2)));
		List.of(testFilm1, testFilm2, testFilm3).forEach(e -> filmService.addFilm(e));

		assertEquals(List.of(testFilm3, testFilm1),
				filmService.getSortFilms(createdDirector2.getId(), "year"));

		filmService.deleteAllFilms();
		directorStorage.deleteDirector(createdDirector1.getId());
		directorStorage.deleteDirector(createdDirector2.getId());
	}

	@Test
	void checkSortRate() {
		Director createdDirector1 = directorStorage.createDirector(director1);
		Director createdDirector2 = directorStorage.createDirector(director2);
		testFilm1.setDirectors(new HashSet<>(List.of(createdDirector1, createdDirector2)));
		testFilm2.setDirectors(new HashSet<>(List.of(createdDirector1)));
		testFilm3.setDirectors(new HashSet<>(List.of(createdDirector2)));
		List.of(testFilm1, testFilm2, testFilm3).forEach(e -> filmService.addFilm(e));

		assertEquals(List.of(testFilm2, testFilm1),
				filmService.getSortFilms(createdDirector1.getId(), "likes"));

		filmService.deleteAllFilms();
		directorStorage.deleteDirector(createdDirector1.getId());
		directorStorage.deleteDirector(createdDirector2.getId());
	}
}

