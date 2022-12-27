package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.DirectorStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Director;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
	private final DirectorStorage directorStorage;

	public Director getById(int id) {
		return directorStorage.getById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Невозможно получить режиссера с id=%d", id)));
	}

	public List<Director> getAllDirectors() {
		return directorStorage.getAllDirectors();
	}

	public Director createDirector(Director director) {
		return directorStorage.createDirector(director);
	}

	public Director updateDirector(Director director) {
		return directorStorage.updateDirector(director);
	}

	public void deleteDirector(int id) {
		directorStorage.deleteDirector(id);
	}
}