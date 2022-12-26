package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
	Optional<Genre> getById(int id);

	List<Genre> getAllGenres();
}
