package ru.yandex.practicum.filmrate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmrate.dao.MpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {
	private final MpaStorage mpaStorage;

	@Test
	void getMpaById() {
		assertEquals("G", mpaStorage.getMpaById(1).get().getName());
		assertEquals("NC-17", mpaStorage.getMpaById(5).get().getName());
	}

	@Test
	void getAllMpas() {
		assertEquals(5, mpaStorage.getAllMpa().size());
	}
}
