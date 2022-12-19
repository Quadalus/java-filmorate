package ru.yandex.practicum.filmrate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmrate.dao.FilmStorage;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmStorageTest {
    private final FilmStorage filmStorage;

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
        filmStorage.addFilm(testFilm);


        Film updatedFilm = Film.builder()
                .id(1)
                .name("Test Film Name")
                .description("test updated film 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .rate(4)
                .mpa(new Mpa(1, "NC-17"))
                .build();
        filmStorage.updateFilm(updatedFilm);
        assertEquals("NC-17", updatedFilm.getMpa().getName());
        assertEquals(1, updatedFilm.getId());
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
}
