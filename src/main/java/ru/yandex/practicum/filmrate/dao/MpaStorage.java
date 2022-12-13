package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    Optional<Mpa> getMpaById(int id);

    List<Mpa> getAllMpa();
}
