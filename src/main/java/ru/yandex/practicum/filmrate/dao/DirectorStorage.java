package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

	Optional<Director> getById(int id);

	List<Director> getAllDirectors();

	Director createDirector(Director director);

	Director updateDirector(Director director);

	void deleteDirector(int id);
}