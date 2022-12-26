package ru.yandex.practicum.filmrate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmrate.dao.GenreStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {
	private final GenreStorage genreStorage;

	@Test
	void getGenreById() {
		assertEquals("Комедия", genreStorage.getById(1).get().getName());
		assertEquals("Боевик", genreStorage.getById(6).get().getName());
	}

	@Test
	void getAllGenres() {
		assertEquals(6, genreStorage.getAllGenres().size());
	}
}
