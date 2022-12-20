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
public class DirectorController {

	private final DirectorService directorService;

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Director getDirectorById(@PathVariable int id) {
		Director director = directorService.getById(id);
		log.info("Режиссер с id={} получен.", director.getId());
		return director;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
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
	@ResponseStatus(HttpStatus.OK)
	public Director updateDirector(@Valid @RequestBody Director director) {
		Director updatedDirector = directorService.updateDirector(director);
		log.info("Режиссер с id={} обновлён.", updatedDirector.getId());
		return updatedDirector;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteDirector(@PathVariable int id) {
		directorService.deleteDirector(id);
		log.info("Режиссер с id={} удален.", id);
	}
}