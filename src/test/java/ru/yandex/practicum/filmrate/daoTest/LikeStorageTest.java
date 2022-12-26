package ru.yandex.practicum.filmrate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmrate.dao.FilmStorage;
import ru.yandex.practicum.filmrate.dao.LikeStorage;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.Genre;
import ru.yandex.practicum.filmrate.model.Mpa;
import ru.yandex.practicum.filmrate.model.User;
import ru.yandex.practicum.filmrate.service.FilmService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LikeStorageTest {
	private final LikeStorage likeStorage;
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;

	private final FilmService filmService;

	@Test
	void addLike() {
		Film testFilm = Film.builder()
				.name("Test Film Name")
				.description("test film 1")
				.releaseDate(LocalDate.of(2022, 1, 1))
				.duration(100)
				.mpa(new Mpa(1, "R"))
				.build();
		filmStorage.addFilm(testFilm);

		User user = User.builder()
				.id(1)
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);
		var filmRate = filmStorage.getFilmById(1).get().getRate();
		assertEquals(0, filmRate);

		likeStorage.addLike(testFilm.getId(), user.getId());
		filmRate = filmStorage.getFilmById(1).get().getRate();
		assertEquals(1, filmRate);
	}

	@Test
	void deleteLike() {
		Film testFilm = Film.builder()
				.name("Test Film Name")
				.description("test film 1")
				.releaseDate(LocalDate.of(2022, 1, 1))
				.duration(100)
				.mpa(new Mpa(1, "R"))
				.build();
		filmStorage.addFilm(testFilm);

		User user = User.builder()
				.id(1)
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);
		var filmRate = filmStorage.getFilmById(1).get().getRate();
		assertEquals(0, filmRate);

		likeStorage.addLike(testFilm.getId(), user.getId());
		filmRate = filmStorage.getFilmById(1).get().getRate();
		assertEquals(1, filmRate);

		likeStorage.deleteLike(testFilm.getId(), user.getId());
		filmRate = filmStorage.getFilmById(1).get().getRate();
		assertEquals(0, filmRate);
	}

	@Test
	void getTopFilms() {
		Film testFilm = Film.builder()
				.name("Test Film Name")
				.description("test film 1")
				.releaseDate(LocalDate.of(2022, 1, 1))
				.duration(100)
				.mpa(new Mpa(1, "R"))
				.build();
		Film testFilmSave = filmService.addFilm(testFilm);

		Film testFilm2 = Film.builder()
				.name("Test Film Name 2")
				.description("test film 2")
				.releaseDate(LocalDate.of(2021, 11, 11))
				.duration(111)
				.genres(new HashSet<>(Arrays.asList(new Genre(1, "Comedy"), new Genre(2, "Drama"))))
				.mpa(new Mpa(2, "PG"))
				.build();
		Film testFilmSave2 = filmService.addFilm(testFilm2);

		User user = User.builder()
				.id(1)
				.email("nick@email.com")
				.login("nick")
				.name("name")
				.birthday(LocalDate.of(1990, 10, 4))
				.build();
		userStorage.addUser(user);

		User user2 = User.builder()
				.id(2)
				.email("nick2@email.com")
				.login("nick2")
				.name("name2")
				.birthday(LocalDate.of(1994, 10, 4))
				.build();
		userStorage.addUser(user2);

		likeStorage.addLike(testFilm.getId(), user.getId());
		likeStorage.addLike(testFilm2.getId(), user2.getId());

		var likedFilms = likeStorage.getTopFilms(10, 0, 0).stream()
				.map(filmStorage.getFilmMap()::get)
				.collect(Collectors.toList());

		assertEquals(2, likedFilms.size());

		likedFilms = likeStorage.getTopFilms(10, 0, 2022).stream()
				.map(filmStorage.getFilmMap()::get)
				.collect(Collectors.toList());

		assertEquals(1, likedFilms.size());
		assertEquals(testFilmSave, likedFilms.get(0));

		likedFilms = likeStorage.getTopFilms(10, 1, 0).stream()
				.map(filmStorage.getFilmMap()::get)
				.collect(Collectors.toList());

		assertEquals(1, likedFilms.size());
		assertEquals(testFilmSave2, likedFilms.get(0));
	}
}
