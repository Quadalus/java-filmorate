package ru.yandex.practicum.filmrate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.Director;
import ru.yandex.practicum.filmrate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class DirectorController {

	private final DirectorService directorService;

	@GetMapping("/{id}")
	public Director getDirectorById(@PathVariable int id) {
		Director director = directorService.getById(id);
		log.info("Режиссер с id={} получен.", director.getId());
		return director;
	}

	@GetMapping
	public List<Director> getAllDirectors() {
		log.info("Список режиссеров получен.");
		return directorService.getAllDirectors();
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public Director createDirector(@Valid @RequestBody Director director) {
		Director addDirector = directorService.createDirector(director);
		log.info("Режиссер с id={} был добавлен.", addDirector.getId());
		return addDirector;
	}

	@PutMapping
	public Director updateDirector(@Valid @RequestBody Director director) {
		Director updatedDirector = directorService.updateDirector(director);
		log.info("Режиссер с id={} обновлён.", updatedDirector.getId());
		return updatedDirector;
	}

	@DeleteMapping("/{id}")
	public void deleteDirector(@PathVariable int id) {
		directorService.deleteDirector(id);
		log.info("Режиссер с id={} удален.", id);
	}
}