package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.MpaStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa findMpaById(int id) {
        return mpaStorage.getMpaById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Mpa с id=%d нет", id)));
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
