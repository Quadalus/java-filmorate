package ru.yandex.practicum.filmrate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmrate.model.Event;
import ru.yandex.practicum.filmrate.service.FeedService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FeedController {
	private final FeedService feedService;

	@GetMapping("/users/{id}/feed")
	@ResponseStatus(HttpStatus.OK)
	public List<Event> getUserEvents(@PathVariable int id) {
		log.info("Лента событий для пользователя с id={} получена", id);
		return feedService.getUserEvents(id);
	}
}