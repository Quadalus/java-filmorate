package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.GenreStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(int id) {
        return genreStorage.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Невозможно получить пользователя с id=%d", id)));
    }

    public List<Genre> getAllGenre() {
        return genreStorage.getAllGenres();
    }
}
