package ru.yandex.practicum.filmrate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.Mpa;
import ru.yandex.practicum.filmrate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa findMpaById(@PathVariable int id) {
        log.info("Mpa с {} получен.", id);
        return mpaService.findMpaById(id);
    }

    @GetMapping
    public List<Mpa> findAllMpa() {
        log.info("Список с mpa получен.");
        return mpaService.getAllMpa();
    }
}
