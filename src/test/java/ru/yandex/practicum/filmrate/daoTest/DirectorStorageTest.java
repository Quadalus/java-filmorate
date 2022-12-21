package ru.yandex.practicum.filmrate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmrate.model.Director;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorStorageTest {

	private final ru.yandex.practicum.filmrate.dao.DirectorStorage directorStorage;

	@Test
	void checkGetById() {
		Director actualDirector = directorStorage.createDirector(new Director("Director1"));
		assertEquals(new Director(actualDirector.getId(), "Director1"), actualDirector);
		directorStorage.deleteDirector(actualDirector.getId());
	}

	@Test
	void checkGetAllDirectors() {
		Director director1 = directorStorage.createDirector(new Director("Director1"));
		Director director2 = directorStorage.createDirector(new Director("Director2"));
		assertEquals(2, directorStorage.getAllDirectors().size());
		assertTrue(directorStorage.getAllDirectors().contains(new Director(director1.getId(), "Director1")));
		assertTrue(directorStorage.getAllDirectors().contains(new Director(director2.getId(), "Director2")));
		directorStorage.deleteDirector(director1.getId());
		directorStorage.deleteDirector(director2.getId());
	}

	@Test
	void checkCreateDirector() {
		Director director = directorStorage.createDirector(new Director("Director1"));
		assertTrue(directorStorage.getAllDirectors().contains(new Director(director.getId(), "Director1")));
		directorStorage.deleteDirector(director.getId());
	}

	@Test
	void checkUpdateDirector() {
		Director director = directorStorage.createDirector(new Director("Director1"));
		directorStorage.updateDirector(new Director(director.getId(), "Update name"));
		assertTrue(directorStorage.getAllDirectors().contains(new Director(director.getId(), "Update name")));
		directorStorage.deleteDirector(director.getId());
	}

	@Test
	void checkDeleteDirector() {
		Director director = directorStorage.createDirector(new Director("Director1"));
		directorStorage.deleteDirector(director.getId());
		assertTrue(directorStorage.getAllDirectors().isEmpty());
	}
}