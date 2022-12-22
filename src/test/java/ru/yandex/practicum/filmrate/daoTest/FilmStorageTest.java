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
import ru.yandex.practicum.filmrate.model.Mpa;
import ru.yandex.practicum.filmrate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Test
    void createFilm() {
        Film testFilm = Film.builder()
                .name("Test Film Name")
                .description("test film 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(4)
                .mpa(new Mpa(1, "R"))
                .build();

        filmStorage.addFilm(testFilm);

        assertEquals("R", testFilm.getMpa().getName());
        assertEquals(1, testFilm.getId());
        assertEquals(1, filmStorage.getListFilms().size());

        System.out.println(testFilm);
    }

    @Test
    void updateFilm() {
        Film testFilm = Film.builder()
                .name("Test Film Name")
                .description("test film 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(4)
                .mpa(new Mpa(1, "R"))
                .build();
        Film film = filmStorage.addFilm(testFilm);


        Film updatedFilm = Film.builder()
                .id(film.getId())
                .name("Test Film Name")
                .description("test updated film 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(4)
                .mpa(new Mpa(1, "NC-17"))
                .build();
        filmStorage.updateFilm(updatedFilm);
        assertEquals("NC-17", updatedFilm.getMpa().getName());
        assertEquals(film.getId(), updatedFilm.getId());
        assertEquals(1, filmStorage.getListFilms().size());

        System.out.println(updatedFilm);
    }

    @Test
    void findAllFilms() {
        Film testFilm = Film.builder()
                .name("Test Film Name")
                .description("test film 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(4)
                .mpa(new Mpa(1, "R"))
                .build();
        filmStorage.addFilm(testFilm);

        Film testFilm2 = Film.builder()
                .name("Test Film Name 2")
                .description("test film 2")
                .releaseDate(LocalDate.of(2022, 11, 11))
                .duration(111)
                .rate(3)
                .mpa(new Mpa(2, "PG"))
                .build();
        filmStorage.addFilm(testFilm2);

        assertEquals(2, filmStorage.getListFilms().size());
    }

    @Test
    void getFilmById() {
        Film testFilm = Film.builder()
                .name("Test Film Name")
                .description("test film 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(4)
                .mpa(new Mpa(1, "R"))
                .build();
        filmStorage.addFilm(testFilm);

        Film filmById = filmStorage.getFilmById(1).get();
        assertEquals("Test Film Name", filmById.getName());
    }

    @Test
    void deleteFilmById() {
        Film testFilm = Film.builder()
                .name("Test Film Name")
                .description("test film 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(4)
                .mpa(new Mpa(1, "R"))
                .build();
        filmStorage.addFilm(testFilm);

        Film testFilm2 = Film.builder()
                .name("Test Film Name 2")
                .description("test film 2")
                .releaseDate(LocalDate.of(2022, 11, 11))
                .duration(111)
                .rate(3)
                .mpa(new Mpa(2, "PG"))
                .build();
        filmStorage.addFilm(testFilm2);

        assertEquals(2, filmStorage.getListFilms().size());
        filmStorage.deleteFilm(2);
        assertEquals(1, filmStorage.getListFilms().size());
        filmStorage.deleteFilm(1);
        assertEquals(0, filmStorage.getListFilms().size());
    }

    @Test
    void getRecommendedFilms(){
        Film testFilm = Film.builder()
                .name("Test Film Name")
                .description("test film 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(4)
                .mpa(new Mpa(1, "R"))
                .build();
        filmStorage.addFilm(testFilm);

        Film testFilm2 = Film.builder()
                .name("Test Film Name 2")
                .description("test film 2")
                .releaseDate(LocalDate.of(2022, 11, 11))
                .duration(111)
                .rate(3)
                .mpa(new Mpa(2, "PG"))
                .build();
        filmStorage.addFilm(testFilm2);

        Film testFilm3 = Film.builder()
                .name("Test Film Name 3")
                .description("test film 3")
                .releaseDate(LocalDate.of(2022, 12, 11))
                .duration(121)
                .rate(2)
                .mpa(new Mpa(3, "PG-13"))
                .build();
        filmStorage.addFilm(testFilm3);

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

        likeStorage.addLike(1, 1);
        likeStorage.addLike(2, 2);
        likeStorage.addLike(3, 1);
        likeStorage.addLike(3, 2);

        assertEquals(filmStorage.getFilmById(1).orElse(null), filmStorage.getRecommendedFilms(2).get(0));
        assertEquals(filmStorage.getFilmById(2).orElse(null), filmStorage.getRecommendedFilms(1).get(0));
    }
}
