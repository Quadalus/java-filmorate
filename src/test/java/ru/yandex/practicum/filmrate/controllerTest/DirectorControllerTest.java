package ru.yandex.practicum.filmrate.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmrate.controller.DirectorController;
import ru.yandex.practicum.filmrate.model.Director;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DirectorControllerTest {
	private final DirectorController directorController;

	@Autowired
	public DirectorControllerTest(DirectorController directorController) {
		this.directorController = directorController;
	}

	@Test
	void checkGetDirectorById() {
		Director actualDirector = directorController.createDirector(new Director("Director1"));
		assertEquals(new Director(actualDirector.getId(), "Director1"), actualDirector);
		directorController.deleteDirector(actualDirector.getId());
	}

	@Test
	void checkGetAllDirectors() {
		Director director1 = directorController.createDirector(new Director("Director1"));
		Director director2 = directorController.createDirector(new Director("Director2"));
		assertEquals(2, directorController.getAllDirectors().size());
		assertTrue(directorController.getAllDirectors().contains(new Director(director1.getId(), "Director1")));
		assertTrue(directorController.getAllDirectors().contains(new Director(director2.getId(), "Director2")));
		directorController.deleteDirector(director1.getId());
		directorController.deleteDirector(director2.getId());
	}

	@Test
	void checkCreateDirector() {
		Director director = directorController.createDirector(new Director("Director1"));
		assertTrue(directorController.getAllDirectors().contains(new Director(director.getId(), "Director1")));
		directorController.deleteDirector(director.getId());
	}

	@Test
	void checkUpdateDirector() {
		Director director = directorController.createDirector(new Director("Director1"));
		directorController.updateDirector(new Director(director.getId(), "Update name"));
		assertTrue(directorController.getAllDirectors().contains(new Director(director.getId(), "Update name")));
		directorController.deleteDirector(director.getId());
	}

	@Test
	void checkDeleteDirector() {
		Director director = directorController.createDirector(new Director("Director1"));
		directorController.deleteDirector(director.getId());
		assertTrue(directorController.getAllDirectors().isEmpty());
	}
}