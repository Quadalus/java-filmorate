package ru.yandex.practicum.filmrate.controllerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmrate.controller.FilmController;
import ru.yandex.practicum.filmrate.exception.ValidationException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.service.FilmService;
import ru.yandex.practicum.filmrate.service.ValidationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController filmController;
    private FilmService filmService;
    private ValidationService validationService;

    /*@BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }*/

    @AfterEach
    public void tearDown() {
        filmController = null;
    }

    @Test
    public void ShouldBeNotAddFilmWithoutName() {
        Film film = Film.builder()
                .name("")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу.")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(178)
                .build();

        Film film1 = Film.builder()
                .name(" ")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу.")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(178)
                .build();

        Film film2 = Film.builder()
                .name(null)
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу.")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(178)
                .build();

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
    }

    @Test
    public void ShouldBeNotAddFilmWithWrongDescriptionLength() {
        Film film = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу" +
                        ".Алаолаолаолаалоаллааааааав")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(178)
                .build();

        Film film1 = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу" +
                        ".Алаолаолаолаалоаллааааааав1")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(178)
                .build();

        Film film2 = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу" +
                        ".Алаолаолаолаалоаллааааааав12")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(178)
                .build();

        assertEquals(200, film.getDescription().length());
        assertEquals(201, film1.getDescription().length());
        assertEquals(202, film2.getDescription().length());
        assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
    }

    @Test
    public void ShouldBeNotAddFilmWithNegativeDuration() {
        Film film = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(0)
                .build();

        Film film1 = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(-1)
                .build();

        Film film2 = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу")
                .releaseDate(LocalDate.of(2001, 12, 19))
                .duration(-2)
                .build();

        assertEquals(0, film.getDuration());
        assertEquals(-1, film1.getDuration());
        assertEquals(-2, film2.getDuration());
        assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
    }

    @Test
    public void ShouldBeNotAddFilmWithEarlyReleaseDate() {
        Film film = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(0)
                .build();

        Film film1 = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу")
                .releaseDate(LocalDate.of(1422, 12, 19))
                .duration(-1)
                .build();

        Film film2 = Film.builder()
                .name("Властелин колец: Братство кольца.")
                .description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
                        " Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу")
                .releaseDate(LocalDate.of(1824, 12, 19))
                .duration(-2)
                .build();

        assertEquals(LocalDate.of(1895, 12, 28), film.getReleaseDate());
        assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
    }
}
